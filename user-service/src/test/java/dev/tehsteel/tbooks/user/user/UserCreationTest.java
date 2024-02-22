package dev.tehsteel.tbooks.user.user;

import dev.tehsteel.tbooks.user.cache.CacheService;
import dev.tehsteel.tbooks.user.user.model.User;
import dev.tehsteel.tbooks.user.user.model.request.UserRegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserCreationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserCreationTest.class);

	@Autowired
	private UserService userService;

	@Autowired
	private CacheService cacheService;

	@Test
	public void testUserCreation() {
		LOGGER.info("Creating a test user");

		final UserRegisterRequest userRequest = new UserRegisterRequest("test@tbooks.com", "Test", "test");
		userService.createUser(userRequest);

		LOGGER.info("Created a test user: {}", userRequest.email());

		testCache();

		LOGGER.info("Deleting a test user: {}", userRequest.email());

	}


	private void testCache() {
		Assertions.assertEquals(cacheService.getCachedData("users", "test@tbooks.com", User.class).getEmail(), "test@tbooks.com", "User was not found in cache");
	}
}
