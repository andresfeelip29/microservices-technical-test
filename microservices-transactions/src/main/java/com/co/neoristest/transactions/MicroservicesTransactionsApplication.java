package com.co.neoristest.transactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication(exclude = {WebMvcAutoConfiguration.class, DataSourceAutoConfiguration.class })
public class MicroservicesTransactionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicesTransactionsApplication.class, args);
	}

}
