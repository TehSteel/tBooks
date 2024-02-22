package dev.tehsteel.tbooks.gateway;

import dev.tehsteel.tbooks.common.util.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan
public class GatewayServiceApplication {


	public static void main(final String[] args) {
		JwtUtil.setJwtToken("q59quD4lFpQK#*uu^2@tc6bett@hUqiF");
		SpringApplication.run(GatewayServiceApplication.class, args);
	}


}
