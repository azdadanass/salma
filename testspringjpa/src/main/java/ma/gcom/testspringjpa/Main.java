package ma.gcom.testspringjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = { "ma.gcom.testspringjpa" })
@ComponentScan(basePackages = { "ma.gcom.testspringjpa" })
@EnableGlobalMethodSecurity(securedEnabled = true)
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
