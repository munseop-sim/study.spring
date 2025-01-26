package study.ms2709.queue.waitingroom.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import study.ms2709.queue.waitingroom.config.exception.WaitingRoomException;

@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(WaitingRoomException.class)
	Mono<ResponseEntity<ServerExceptionResponse>> applicationExceptionHandler(
		WaitingRoomException ex) {
		return Mono.just(ResponseEntity
			.status(HttpStatus.OK)
			.body(new ServerExceptionResponse(ex.getMessage())));
	}

	public record ServerExceptionResponse(String reason) {

	}
}
