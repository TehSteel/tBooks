package dev.tehsteel.tbooks.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServiceApplication {

	public static void main(final String[] args) {
		SpringApplication.run(ConfigServiceApplication.class, args);
	}
}
