package dev.tehsteel.tbooks.review.controller;

import dev.tehsteel.tbooks.common.user.UserObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/review")
public class ReviewController {

	private final RestTemplate restTemplate;


	@GetMapping("/test")
	public String getCurrentUserName(final HttpServletRequest request) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", request.getHeader("Authorization"));
		final HttpEntity<String> entity = new HttpEntity<>(headers);
		final ResponseEntity<UserObject> response = restTemplate.exchange("http://user-service/api/user/get-user", HttpMethod.GET, entity, UserObject.class);

		restTemplate.getForEntity("http://user-service/api/user/get-user", UserObject.class);
		return response.getBody().getName();
	}
}
