package ms2709.reactor;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class PublishWithOperator2Test {

	private final PublishWithOperator2 sut = new PublishWithOperator2();

	@Test
	void fluxWithMerge() {
		StepVerifier.create(sut.fluxWithMerge())
			.expectNext("a", "b", "c", "d", "e", "f")
			.verifyComplete();
	}

	@Test
	void monoWithMerge() {
		StepVerifier.create(sut.monoWithMerge())
			.expectNext("a", "b", "c")
			.verifyComplete();
	}

	@Test
	void fluxWithZip() {
		StepVerifier.create(sut.fluxWithZip())
			.expectNext("ad", "be", "cf")
			.verifyComplete();
	}

	@Test
	void monoWithZip() {
		StepVerifier.create(sut.monoWithZip())
			.expectNext(6)
			.verifyComplete();
	}

	@Test
	void fluxCount() {
		StepVerifier.create(sut.fluxCount())
			.expectNext(10L)
			.verifyComplete();
	}

	@Test
	void fluxDistinct() {
		StepVerifier.create(sut.fluxDistinct())
			.expectNext("a", "b", "c")
			.verifyComplete();
	}

	@Test
	void fluxReduce() {
		StepVerifier.create(sut.fluxReduce())
			.expectNext(55)
			.verifyComplete();
	}

	@Test
	void fluxGroupBy() {
		StepVerifier.create(sut.fluxGroupBy())
			.expectNext(30)
			.expectNext(25)
			.verifyComplete();

		StepVerifier.create(sut.fluxGroupBy())
			.expectNextCount(2)
			.verifyComplete();
	}

	@Test
	void fluxDelayAndLimit() {
		StepVerifier.create(sut.fluxDelayAndLimit())
			.expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
			.verifyComplete();
	}

	@Test
	void fluxSample() {
		StepVerifier.create(sut.fluxSample())
			.expectNextCount(35L)
			.verifyComplete();

	}
}