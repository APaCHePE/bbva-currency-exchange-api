package com.bbva.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class ExchangeApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ExchangeApplication.class);
		ConfigurableEnvironment env = app.run(args).getEnvironment();
		System.out.println("🚀 La aplicación se ha iniciado en el puerto: " + env.getProperty("server.port"));
	}

}
