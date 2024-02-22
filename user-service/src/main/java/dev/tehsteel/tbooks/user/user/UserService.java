package dev.tehsteel.tbooks.user.user;

import dev.tehsteel.tbooks.user.cache.CacheService;
import dev.tehsteel.tbooks.user.user.model.User;
import dev.tehsteel.tbooks.user.user.model.request.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {


	private final UserRepository userRepository;
	private final CacheService cacheService;
	private final PasswordEncoder passwordEncoder;

	public User createUser(final UserRegisterRequest userRegisterRequest) {
		log.debug("Creating new user by: {}", userRegisterRequest.email());
		if (userRepository.findByEmail(userRegisterRequest.email()).isPresent()) {
			log.debug("User is already created by email: {}", userRegisterRequest.email());
			return null;
		}

		final User user = new User();
		user.setName(userRegisterRequest.username());
		user.setEmail(userRegisterRequest.email());
		user.setPassword(passwordEncoder.encode(userRegisterRequest.password()));

		userRepository.insert(user);
		log.debug("User {} inserted into the repository", user.getEmail());

		cacheService.insertValue("users", userRegisterRequest.email(), user);
		log.debug("User {} cached", user.getEmail());

		return user;
	}

	public User getUserByEmail(final String email) {
		User user = cacheService.getCachedData("users", email, User.class);
		if (user != null) {
			log.debug("User found in cache: {}", user.getName());
			return user;
		}

		log.debug("User not found in cache. Querying repository...");
		user = userRepository.findByEmail(email).orElse(null);

		if (user != null) {
			log.debug("User found in repository: {}", user.getName());
			cacheService.insertValue("users", email, user);
		} else {
			log.debug("User not found in repository.");
		}

		return user;
	}

}
