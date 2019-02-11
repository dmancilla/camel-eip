package cl.dman.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CamelEipApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamelEipApplication.class, args);
	}

	@Bean
	CamelContextConfiguration contextConfiguration() {
		return new CamelContextConfiguration() {

			@Override
			public void beforeApplicationStart(CamelContext camelContext) {
				camelContext.setStreamCaching(true);
			}

			@Override
			public void afterApplicationStart(CamelContext camelContext) {

			}
		};
	}
}
