package ms2709.reactor;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class SchedulerTest {

	private final Scheduler sut = new Scheduler();

	@Test
	void fluxMapWithSubscribeOn() {
		StepVerifier.create(sut.fluxMapWithSubscribeOn())
			.expectNextCount(10)
			.verifyComplete();
	}

	@Test
	void fluxMapWithPublishOn() {
		StepVerifier.create(sut.fluxMapWithPublishOn())
			.expectNextCount(10)
			.verifyComplete();
	}
}