package study.ms2709.queue.waitingroom.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import ms2709.study.LogExtensionKt;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;
import study.ms2709.queue.waitingroom.config.exception.WaitingRoomException;
import study.ms2709.queue.waitingroom.config.exception.WaitingRoomException.ErrorType;

@Service
public class UserQueueServiceImpl implements UserQueueService {

	private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
	private final String USER_QUEUE_WAIT_KEY = "users:queue:%s:wait";
	private final String USER_QUEUE_WAIT_KEY_FOR_SCAN = "users:queue:*:wait";
	private final String USER_QUEUE_PROCEED_KEY = "users:queue:%s:proceed";
	private final Logger log = LogExtensionKt.logger(UserQueueServiceImpl.class);
	@Value("${scheduler.enabled}")
	private Boolean scheduling = false;

	public UserQueueServiceImpl(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
		this.reactiveRedisTemplate = reactiveRedisTemplate;
	}

	@Override
	public Mono<Long> registerWaitQueue(String queue, Long userId) {
		var unixTimestamp = Instant.now().getEpochSecond();
		return reactiveRedisTemplate.opsForZSet()
			.add(USER_QUEUE_WAIT_KEY.formatted(queue), userId.toString(), unixTimestamp)
			.filter(i -> {
				log.info("i: {}", i);
				return i;
			})
			.switchIfEmpty(Mono.error(
				WaitingRoomException.build(ErrorType.ALREADY_REGISTERED_USER, userId.toString())))
			.flatMap(i -> reactiveRedisTemplate.opsForZSet()
				.rank(USER_QUEUE_WAIT_KEY.formatted(queue), userId.toString()))
			.map(i -> i >= 0 ? i + 1 : i);
	}

	@Override
	public Mono<Long> allowUser(String queue, Long count) {
		return reactiveRedisTemplate.opsForZSet()
			.popMin(USER_QUEUE_WAIT_KEY.formatted(queue), count)
			.flatMap(member -> reactiveRedisTemplate.opsForZSet()
				.add(USER_QUEUE_PROCEED_KEY.formatted(queue), member.getValue(),
					Instant.now().getEpochSecond()))
			.count();
	}

	@Override
	public Mono<Boolean> isAllowed(String queue, Long userId) {
		return reactiveRedisTemplate.opsForZSet()
			.rank(USER_QUEUE_PROCEED_KEY.formatted(queue), userId.toString())
			.defaultIfEmpty(-1L)
			.map(rank -> rank >= 0);
	}

	@Override
	public Mono<Boolean> isAllowedByToken(String queue, Long userId,
		String token) {
		return this.generateToken(queue, userId)
			.filter(gen -> gen.equalsIgnoreCase(token))
			.map(i -> true)
			.defaultIfEmpty(false);
	}

	@Override
	public Mono<Long> getRank(String queue, Long userId) {
		return reactiveRedisTemplate.opsForZSet()
			.rank(USER_QUEUE_WAIT_KEY.formatted(queue), userId.toString())
			.defaultIfEmpty(-1L)
			.map(rank -> rank >= 0 ? rank + 1 : rank);
	}

	@Scheduled(initialDelay = 5000, fixedDelay = 10000)
	@Override
	public void scheduleAllowUser() {
		if (!scheduling) {
			log.info("passed scheduling...");
			return;
		}
		log.info("called scheduling...");

		var maxAllowUserCount = 100L;
		reactiveRedisTemplate.scan(ScanOptions.scanOptions()
				.match(USER_QUEUE_WAIT_KEY_FOR_SCAN)
				.count(100)
				.build())
			.map(key -> key.split(":")[2])
			.flatMap(queue -> allowUser(queue, maxAllowUserCount).map(
				allowed -> Tuples.of(queue, allowed)))
			.doOnNext(tuple -> log.info(
				"Tried %d and allowed %d members of %s queue".formatted(maxAllowUserCount,
					tuple.getT2(), tuple.getT1())))
			.subscribe();
	}

	@Override
	public Mono<String> generateToken(final String queue, final Long userId) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			var input = "user-queue-%s-%d".formatted(queue, userId);
			byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

			StringBuilder hexString = new StringBuilder();
			for (byte aByte : encodedHash) {
				hexString.append(String.format("%02x", aByte));
			}
			return Mono.just(hexString.toString());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
