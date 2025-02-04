package study.ms2709.queue.waitingroom.config;

import ms2709.study.LogExtensionKt;
import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

@Configuration
public class RedisConfig implements ApplicationListener<ApplicationReadyEvent> {

	private final Logger log = LogExtensionKt.logger(RedisConfig.class);
	private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

	public RedisConfig(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
		this.reactiveRedisTemplate = reactiveRedisTemplate;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		reactiveRedisTemplate.opsForValue().get("1")
			.doOnSuccess(i -> log.info("Initialize to redis connection"))
			.doOnError(
				(err) -> log.error("Failed to initialize redis connection: {}", err.getMessage()))
			.subscribe();

	}

//	@Bean
//	public RedisConnectionFactory jedisConnectionFactory() {
//		RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
//			.master("mymaster")
//			.sentinel("localhost", 26379)
//			.sentinel("localhost", 26380)
//			.sentinel("localhost", 26381);
//
//		return new JedisConnectionFactory(sentinelConfig);
//	}

//	/**
//	 * Lettuce
//	 */
//
//	public RedisConnectionFactory lettuceConnectionFactory() {
//		RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
//			.master("mymaster")
//			.sentinel("127.0.0.1", 26379)
//			.sentinel("127.0.0.1", 26380);
//		return new LettuceConnectionFactory(sentinelConfig);
//	}

}
