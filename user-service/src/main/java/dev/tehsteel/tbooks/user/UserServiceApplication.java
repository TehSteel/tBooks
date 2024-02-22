package dev.tehsteel.tbooks.user;

import dev.tehsteel.tbooks.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoRepositories
public class UserServiceApplication implements CommandLineRunner {

	@Value("${spring.security.jwt_token}")
	private String JwtToken;

	public static void main(final String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {
		JwtUtil.setJwtToken(JwtToken);
	}
}
