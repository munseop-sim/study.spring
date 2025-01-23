package ms2709.reactor;

import ms2709.study.exception.StudyError;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

/**
 * https://projectreactor.io/docs/core/release/reference/testing.html#testing-a-scenario-with-stepverifier
 * StepVerifier를 통한 reactor 테스트
 */
class PublisherTest {

	private final Publisher sut = new Publisher();

	@Test
	void startFluxTest() {
		StepVerifier.create(sut.startFlux())
			.expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
			.verifyComplete();
	}

	@Test
	void flaxFromListTest() {
		StepVerifier.create(sut.flaxFromList())
			.expectNext("a", "b", "c")
			.verifyComplete();
	}

	@Test
	void startMonoTest() {
		StepVerifier.create(sut.startMono())
			.expectNext(10)
			.verifyComplete();
	}

	@Test
	void returnVoidTest() {
		StepVerifier.create(sut.returnVoid())
			.verifyComplete();
	}

	@Test
	void errorTest() {
		StepVerifier.create(sut.returnError())
			.expectError(StudyError.class)
			.verify();
	}

}