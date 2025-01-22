package ms2709.reator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Publisher {

    /**
     *
     * @return 1...10까지 10개
     */
    Flux<Integer> startFlux(){
        return Flux.range(1,10);
    }

    /**
     * @return 10 (1개)
     */
    Mono<Integer> startMono(){
        return Mono.just(10).log();
    }

    /**
     * @return Mono empty
     */
    Mono<?> returnVoid(){
        return Mono.empty().log();
    }
}
