package ms2709.reactor;

import ms2709.study.exception.StudyError;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class PublishWithOperator1Test {

	private final PublishWithOperator1 sut = new PublishWithOperator1();

	@Test
	void fluxWithMap() {
		StepVerifier.create(sut.fluxWithMap())
			.expectNext(2, 4, 6, 8, 10)
			.verifyComplete();
	}

	@Test
	void fluxWithFilter() {
		StepVerifier.create(sut.fluxWithFilter())
			.expectNext(2, 4)
			.verifyComplete();
	}

	@Test
	void fluxWithFilterAndTake() {
		StepVerifier.create(sut.fluxWithFilterAndTake())
			.expectNext(2)
			.verifyComplete();
	}

	@Test
	void fluxWithFlatMap() {
		//출력값 검증
		StepVerifier.create(sut.fluxWithFlatMap())
			.expectNext(10, 11, 12, 13, 14, 20, 21, 22, 23, 24, 30, 31, 32, 33, 34, 40, 41, 42, 43,
				44, 50, 51, 52, 53, 54)
			.verifyComplete();

		//출력 카운트 검즘
		StepVerifier.create(sut.fluxWithFlatMap())
			.expectNextCount(25)
			.verifyComplete();

	}

	@Test
	void fluxTimesTableWithFlatMap() {
		StepVerifier.create(sut.printTimesTableWithFlatMap())
			.expectNextCount(72)
			.verifyComplete();
	}

	@Test
	void fluxTimesTableWithConcatMap() {
		StepVerifier.create(sut.printTimesTableWithConcatMap())
			.expectNextCount(72)
			.verifyComplete();
	}

	@Test
	void monoFlatMapMany() {
		StepVerifier.create(sut.monoFlatMapMany())
			.expectNext(10, 11, 12, 13, 14)
			.verifyComplete();
	}

	@Test
	void monoDefaultIfEmpty() {
		StepVerifier.create(sut.monoDefaultIfEmpty(10))
			.expectNext(10)
			.verifyComplete();
	}

	@Test
	void monoSwitchIfEmpty() {
		StepVerifier.create(sut.monoSwitchIfEmpty(10))
			.expectNext(20)
			.verifyComplete();
	}

	@Test
	void monoSwitchIfEmptyWithError() {
		StepVerifier.create(sut.monoSwitchIfEmptyWithError(10))
			.expectError(StudyError.class)
			.verify();
	}
}