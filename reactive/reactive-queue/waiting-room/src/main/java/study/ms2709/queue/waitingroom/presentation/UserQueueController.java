package study.ms2709.queue.waitingroom.presentation;

import java.time.Duration;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import study.ms2709.queue.waitingroom.domain.dto.AllowUserResponse;
import study.ms2709.queue.waitingroom.domain.dto.AllowedUserResponse;
import study.ms2709.queue.waitingroom.domain.dto.RankNumberResponse;
import study.ms2709.queue.waitingroom.domain.dto.RegisterUserResponse;
import study.ms2709.queue.waitingroom.service.UserQueueService;

@RestController
@RequestMapping("/api/v1/queue")
public class UserQueueController {

	private final UserQueueService userQueueService;


	public UserQueueController(UserQueueService userQueueService) {
		this.userQueueService = userQueueService;
	}

	@PostMapping("")
	public Mono<RegisterUserResponse> registerUser(
		@RequestParam(name = "queue", defaultValue = "default") String queue,
		@RequestParam(name = "user_id") Long userId) {
		return userQueueService.registerWaitQueue(queue, userId)
			.map(RegisterUserResponse::new);
	}

	@PostMapping("/allow")
	public Mono<AllowUserResponse> allowUser(
		@RequestParam(name = "queue", defaultValue = "default") String queue,
		@RequestParam(name = "count") Long count) {
		return userQueueService.allowUser(queue, count)
			.map(allowed -> new AllowUserResponse(count, allowed));
	}

	@GetMapping("/allowed")
	public Mono<AllowedUserResponse> isAllowedUser(
		@RequestParam(name = "queue", defaultValue = "default") String queue,
		@RequestParam(name = "user_id") Long userId,
		@RequestParam(name = "token") String token) {
		return userQueueService.isAllowedByToken(queue, userId, token)
			.map(AllowedUserResponse::new);
	}

	@GetMapping("/rank")
	public Mono<RankNumberResponse> getRankUser(
		@RequestParam(name = "queue", defaultValue = "default") String queue,
		@RequestParam(name = "user_id") Long userId) {
		return userQueueService.getRank(queue, userId)
			.map(RankNumberResponse::new);
	}

	@GetMapping("/touch")
	Mono<?> touch(@RequestParam(name = "queue", defaultValue = "default") String queue,
		@RequestParam(name = "user_id") Long userId,
		ServerWebExchange exchange) {
		return Mono.defer(() -> userQueueService.generateToken(queue, userId))
			.map(token -> {
				exchange.getResponse().addCookie(
					ResponseCookie
						.from("user-queue-%s-token".formatted(queue), token)
						.maxAge(Duration.ofSeconds(300))
						.path("/")
						.build()
				);
				return token;
			});
	}
}
