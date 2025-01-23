package ms2709.reactor;

import ms2709.study.LogExtensionKt;
import org.slf4j.Logger;

public class ReactorStudyMain {

    private static final Logger log = LogExtensionKt.logger(ReactorStudyMain.class);


    public static void main(String[] args) {
        var publisher = new Publisher();

        publisher.startFlux()
            .subscribe(it -> log.info("{}", it));

        publisher.startMono()
            .subscribe();

        publisher.returnVoid()
            .subscribe();
    }
}