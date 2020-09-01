package ma.gcom.testspringjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = { "ma.gcom.testspringjpa" })
@ComponentScan(basePackages = { "ma.gcom.testspringjpa" })
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
