package study.ms2709.queue.website.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import study.ms2709.queue.website.domain.dto.AllowedUserResponse;

@Controller
public class IndexController {

	RestTemplate restTemplate = new RestTemplate();

	@Value("${custom.waiting-room-url}")
	private String waitingRoomUrl;

	@Value("${custom.waiting-server}")
	private String waitingServer;

	@Value("${server.port}")
	private String thisPort;

	@GetMapping("/")
	public String index(@RequestParam(name = "queue", defaultValue = "default") String queue,
		@RequestParam(name = "user_id") Long userId,
		HttpServletRequest request
	) {
		var cookies = request.getCookies();
		var cookieName = "user-queue-%s-token".formatted(queue);

		String token = "";
		if (cookies != null) {
			var cookie = Arrays.stream(cookies)
				.filter(i -> i.getName().equalsIgnoreCase(cookieName)).findFirst();
			token = cookie.orElse(new Cookie(cookieName, "")).getValue();
		}

		var uri = UriComponentsBuilder
			.fromUriString(waitingServer)
			.path("/api/v1/queue/allowed")
			.queryParam("queue", queue)
			.queryParam("user_id", userId)
			.queryParam("token", token)
			.encode()
			.build()
			.toUri();

		ResponseEntity<AllowedUserResponse> response = restTemplate.getForEntity(uri,
			AllowedUserResponse.class);
		if (response.getBody() == null || !response.getBody().allowed()) {
			// 대기 웹페이지로 리다이렉트
			var redirectUrl = "http://127.0.0.1:%s?user_id=%d".formatted(thisPort, userId);
			return "redirect:%s?user_id=%d&redirect_url=%s".formatted(waitingRoomUrl, userId,
				redirectUrl);
		}
		// 허용 상태라면 해당 페이지를 진입
		return "index";
	}

}
