package ms2709.reactor;

import java.time.Duration;
import ms2709.study.LogExtensionKt;
import ms2709.study.exception.StudyError;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 1. map - map 함수를 통하여 1:1 로 반환되는 데이터를 반환
 * <p>
 * 2. filter  - filtering
 * <p>
 * 3. flatMap  - (비동기) 입력된 값을 다른 Publisher로 변환하고 출력을 하나의 `Flux` Stream으로 병합한다는 것이다. 이를 통해 비동기적인,
 * 고수준의 병렬 작업을 구현
 * <p>
 * 4. concatMap - (동기) - `concatMap`이 원소들을 동기적(순차적) 방식으로 처리한다는 것이다. 이 연산자는 입력 Stream의 각 원소에 대해 동기적으로
 * 변환 함수를 적용하고, 이들을 하나의 `Flux` Stream으로 병합한다. 입력 원소의 순서를 보장
 * <p>
 * 5. take / takeLast - 처음(끝)에서 지정된 개수만큼 take
 * <p>
 * 6. flatMapMany  - Mono -> Flux
 * <p>
 * 7. defaultIfEmpty - 연산후에 값이 없을 경우 기본값을 지정하여 반환
 * <p>
 * 8. switchIfEmpty  - 연산후에 값이 없으면 새로운 Mono(Flux)를 생성하여 반환
 */
public class PublishWithOperator1 {

    private static final Logger log = LogExtensionKt.logger(PublishWithOperator1.class);

    //operator - map을 이용
    Flux<Integer> fluxWithMap() {
        return Flux.range(1, 5)
            .map(it -> it * 2);
    }

    Flux<Integer> fluxWithFilter() {
        return Flux.range(1, 5)
            .filter(it -> it % 2 == 0);
    }

    Flux<Integer> fluxWithFilterAndTake() {
        return Flux.range(1, 5)
            .filter(it -> it % 2 == 0)
            .take(1);
    }

    Flux<Integer> fluxWithFlatMap() {
        return Flux.range(1, 5)
            .flatMap(it -> Flux.range(it * 10, 5));
        //1 -> 10,11,12,13,14
        //2 -> 20, 21, 22, 23, 24
        //..
        //5 -> 50, 51, 52, 53, 54
    }

    /**
     * 구구단 출력 - flatMap은 비동기 호출이므로 구구단이 순차적으로 출력되지 않는다. 빠른실행, 순서대로 실행되지 않음
     */
    Flux<Integer> printTimesTableWithFlatMap() {
        return Flux.range(2, 8)
            .flatMap(it1 -> Flux.range(1, 9)
                .delayElements(Duration.ofMillis(100))
                .map(it2 -> {
                        log.info("{} * {} = {}", it1, it2, it1 * it2);
                        return it1 * it2;
                    }
                ));
    }

    /**
     * 구구단 출력 - concatMap은 동기 호출이므로 구구단이 순차적으로 출력된다. flatMap에 비해 상대적으로 느리게 실행, 순서대로 실행됨
     */
    Flux<Integer> printTimesTableWithConcatMap() {
        return Flux.range(2, 8)
            .concatMap(it1 -> Flux.range(1, 9)
                .delayElements(Duration.ofMillis(100))
                .map(it2 -> {
                        log.info("{} * {} = {}", it1, it2, it1 * it2);
                        return it1 * it2;
                    }
                ));
    }

    /**
     * flatMapMany -> Flux로 변환 단일값으로 넘어온 데이터에 대해서 Flux로 변환해서 반환해야 할 때 사용
     */
    Flux<Integer> monoFlatMapMany() {
        return Mono.just(10)
            .flatMapMany(it -> Flux.range(it, 5));
    }

    /**
     * defaultIfEmpty - 지정된 Value를 반환
     */
    Mono<Integer> monoDefaultIfEmpty(Integer min) {
        return Mono.just(100)
            .filter(it -> it < min)
            .defaultIfEmpty(min);
    }

    /**
     * switchIfEmpty - 별도의 Mono를 생성하여 반환
     */
    Mono<Integer> monoSwitchIfEmpty(Integer min) {
        return Mono.just(100)
            .filter(it -> it < min)
            .switchIfEmpty(
                Mono.just(min).map(it -> it * 2)
            );
    }

    /**
     * switchIfEmpty - 지정된 값이 없을 경우 Mono.Error를 반환
     */
    Mono<Integer> monoSwitchIfEmptyWithError(Integer min) {
        return Mono.just(100)
            .filter(it -> it < min)
            .switchIfEmpty(
                Mono.error(new StudyError("에러 반환"))
            );
    }

}
