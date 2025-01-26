package study.ms2709.queue.website;

import ms2709.study.LogExtensionKt;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsiteApplication {

	private static final Logger log = LogExtensionKt.logger(WebsiteApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebsiteApplication.class, args);
		log.info("start reactive-queue website");
	}
}
