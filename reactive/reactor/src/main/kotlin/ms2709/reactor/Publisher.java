package ms2709.reactor;

import java.util.List;
import ms2709.study.exception.StudyError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Publisher {

	/**
	 * @return 1...10까지 10개
	 */
	Flux<Integer> startFlux() {
		return Flux.range(1, 10);
	}

	/**
	 * List에서 값을 Flux로 변환하여 반환
	 */
	Flux<String> flaxFromList() {
		return Flux.fromIterable(List.of("a", "b", "c"));
	}

	/**
	 * @return 10 (1개)
	 */
	Mono<Integer> startMono() {
		return Mono.just(10).log();
	}

	/**
	 * @return Mono empty
	 */
	Mono<?> returnVoid() {
		return Mono.empty().log();
	}

	Mono<?> returnError() {
		return Mono.error(new StudyError("hello mono error")).log();
	}
}
