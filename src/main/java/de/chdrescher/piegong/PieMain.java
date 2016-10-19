package de.chdrescher.piegong;

import de.chdrescher.piegong.service.GpioService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaRepositories
public class PieMain {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(PieMain.class, args);
	}

}
