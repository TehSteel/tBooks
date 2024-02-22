package dev.tehsteel.tbooks.gateway.filter;

import dev.tehsteel.tbooks.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	public AuthenticationFilter() {
		super(Config.class);
	}


	@Override
	public GatewayFilter apply(final Config config) {
		return ((exchange, chain) -> {
			final ServerHttpRequest request = exchange.getRequest();
			final HttpHeaders headers = new HttpHeaders();

			if (authMissing(request)) {
				return onError(exchange, HttpStatus.UNAUTHORIZED);
			}

			final String header = request.getHeaders().getOrEmpty("Authorization").get(0);

			if (header == null || !header.startsWith("Bearer ")) {
				return onError(exchange, HttpStatus.UNAUTHORIZED);
			}

			/* Splitting to get the acutal token */
			final String token = header.split(" ")[1].trim();

			/* If token is empty just return */
			if (token.isEmpty()) {
				return onError(exchange, HttpStatus.UNAUTHORIZED);
			}

			final Claims claims = JwtUtil.getClaims(token);

			/* If the token is expired / null unlucky */
			if (JwtUtil.isExpired(token) || claims == null) {
				return onError(exchange, HttpStatus.UNAUTHORIZED);
			}

			if (JwtUtil.isExpired(token)) {
				return onError(exchange, HttpStatus.UNAUTHORIZED);
			}
			
			headers.putAll(request.getHeaders());
			headers.add("email", JwtUtil.getClaims(token).getSubject());

			final ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(request) {
				@Override
				public HttpHeaders getHeaders() {
					return headers;
				}
			};

			return chain.filter(exchange.mutate().request(mutatedRequest).build());
		});
	}

	private Mono<Void> onError(final ServerWebExchange exchange, final HttpStatus httpStatus) {
		final ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

	private boolean authMissing(final ServerHttpRequest request) {
		return !request.getHeaders().containsKey("Authorization");
	}

	public static class Config {

	}
}