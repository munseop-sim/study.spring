package ms2709.reactor;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * <p>
 * Scheduler - 리액터에서 비동기 코드를 처리하기 위해 사용되는 추상회된 실행 매커니즘 <br />
 * - `immediate()`: 주 스레드에서 작업을 실행. 작업이 스케쥴링되면 즉시 실행 <br />
 * - `single()`: 단일 재사용 가능한 스레드에서 작업을 실행. 모든 작업이 동일한 스레드에서 순차적으로 실행되므로 병렬성은 존재하지 않는다.<br />
 * - `newSingle()`: 각 작업에 대해 새로운 스레드를 생성. 이 스케줄러는 한 번만 사용되고, 작업이 완료되면 스레드를 종료.<br />
 * - `elastic()`: 요청에 따라 스레드 수를 늘리거나 줄일 수 있는 스케줄러. 디폴트로 사용하지 않는 스레드는 기본적으로 60초 후에 종료.<br />
 * - `boundedElastic()`: elastic()와 비슷하지만 워커 스레드의 최대 수가 제한.<br />
 * - `parallel()`: 병렬 작업을 위한 고정 스레드 풀 스케줄러. 디폴트로 CPU 코어 수와 일치하는 고정 스레드 풀을 사용.<br />
 * - `fromExecutorService()`: 사용자가 제공한 `ExecutorService`에서 스레드를 사용합니다.<br />
 * </p>
 * <p>`subscribeOn`: 이 연산자는 스트림이 구독(subscription)을 시작하는 스레드를 결정합니다. 이것을 한번만 사용해도 해당 스트림의 시작 부분에 영향을 미칩니다. 여러 번 호출하더라도 첫 번째 `subscribeOn` 호출이 스트림에 적용됩니다.</p>
 * <p>`publishOn`: 이 연산자는 이후 연산자의 실행을 처리할 스케줄러를 지정합니다. 데이터를 생산하는 작업이 아닌, 그 데이터를 소비하거나 변환하는 작업에 영향을 줍니다. `publishOn` 연산자는 스트림에서 여러 번 호출할 수 있으며, 각 호출은 해당 호출 이후의 연산자들에 대한 스케줄러를 변경</p>
 */
public class Scheduler {

	public Flux<Integer> fluxMapWithSubscribeOn() {
		return Flux.range(1, 10)
			.map(i -> i * 2)
			.subscribeOn(Schedulers.boundedElastic())
			.log();
	}


	public Flux<Integer> fluxMapWithPublishOn() {
		return Flux.range(1, 10)
			.map(i -> i + 1)
			.publishOn(Schedulers.boundedElastic())
			.log()
			.publishOn(Schedulers.parallel())
			.log()
			.map(i -> i * 2);
	}

}
