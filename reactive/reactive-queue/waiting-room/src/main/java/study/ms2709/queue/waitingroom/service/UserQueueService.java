package study.ms2709.queue.waitingroom.service;

import reactor.core.publisher.Mono;

public interface UserQueueService {

	Mono<Long> registerWaitQueue(final String queue, final Long userId);

	Mono<Long> allowUser(final String queue, final Long count);

	@Deprecated(forRemoval = true, since = "'isAllowedByToken'으로 대체됨")
	Mono<Boolean> isAllowed(final String queue, final Long userId);

	Mono<Boolean> isAllowedByToken(final String queue, final Long userId, final String token);

	Mono<Long> getRank(final String queue, final Long userId);

	Mono<String> generateToken(final String queue, final Long userId);

	void scheduleAllowUser();
}
