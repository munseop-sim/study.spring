package ms2709.reactor;

import java.time.Duration;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 1. merge - Mono, Flux에 관계없이 하나의 여러개의 Publlisher를 하나의 Flux로 반환
 * <p>
 * 2. zip - 1. 두 개 혹은 그 이상의 `Publisher`들에서 각각 원소를 가져와서 결합합니다. 결합되는 원소의 개수는 가장 적은 원소를 가진 `Publisher`에
 * 기반하여 결정됩니다. 원소들은 튜플로 결합
 * <p>
 * 3. count - 갯수반환
 * <p>
 * 4. distinct - 중복제거
 * <p>
 * 5. reduce - reduce 연산 수행
 * <p>
 * 6. groupby - grouping 수행
 * <p>
 * 7. limit (back pressure 관련) - Back pressure는 Subscriber가 과도하게 많은 데이터를 받지 않도록 보호하는 메커니즘. `limit`
 * 연산자는 Subscriber가 한 번에 수신할 수 있는 데이터의 양을 제한
 * <p>
 * 8. sample (back pressure 관련) - 1. Back pressure에 사용되며, emit 된 아이템 중 일부만을 선택하여 downstream으로
 * reemit하는 연산. 아이템이 너무 자주 emit되어 처리 할 수 없는 경우 유용하게 사용
 */
public class PublishWithOperator2 {

	/**
	 * Flux.merge
	 */
	Flux<String> fluxWithMerge() {
		return Flux.merge(Flux.just("a", "b", "c"), Flux.just("d", "e", "f"));
	}

	Flux<String> monoWithMerge() {
		return Mono.just("a").mergeWith(Mono.just("b")).mergeWith(Mono.just("c"));
	}

	Flux<String> fluxWithZip() {
		return Flux.zip(Flux.just("a", "b", "c", "z"), Flux.just("d", "e", "f"))
			.map(it -> it.getT1() + it.getT2());
	}

	Mono<Integer> monoWithZip() {
		return Mono.zip(Mono.just(1), Mono.just(2), Mono.just(3))
			.map(i -> i.getT1() + i.getT2() + i.getT3());
	}

	Mono<Long> fluxCount() {
		return Flux.range(1, 10).count();
	}

	Flux<String> fluxDistinct() {
		return Flux.fromIterable(List.of("a", "b", "c", "a", "b", "c"))
			.distinct();
	}

	Mono<Integer> fluxReduce() {
		return Flux.range(1, 10)
			.reduce(0, Integer::sum);
	}

	// groupby
	// 2개의 요소를 가진 Flux반환
	public Flux<Integer> fluxGroupBy() {
		return Flux.range(1, 10)
			.groupBy(i -> (i % 2 == 0) ? "even" : "odd")
			.flatMap(group -> group.reduce(Integer::sum))
			.log();
	}

	// limit
	// 1초 간격으로 2개씩 반환
	public Flux<Integer> fluxDelayAndLimit() {
		return Flux.range(1, 10)
			.delaySequence(Duration.ofSeconds(1))
			.log()
			.limitRate(2);
	}


	/**
	 * sample 1부터 100까지 0.1초 간격으로 반복하는데 0.3초마다 샘플림하여 샘플링된 데이터를 반환
	 */
	public Flux<Integer> fluxSample() {
		return Flux.range(1, 100)
			.delayElements(Duration.ofMillis(100))
			.sample(Duration.ofMillis(300))
			.log();
	}

}
