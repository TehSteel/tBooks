package dev.tehsteel.tbooks.user.user;

import dev.tehsteel.tbooks.common.user.UserObject;
import dev.tehsteel.tbooks.common.util.JwtUtil;
import dev.tehsteel.tbooks.user.user.model.User;
import dev.tehsteel.tbooks.user.user.model.request.UserLoginRequest;
import dev.tehsteel.tbooks.user.user.model.request.UserRegisterRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;

	@PutMapping("/register")
	public ResponseEntity<String> register(@RequestBody final UserRegisterRequest request) {
		final User newUser = userService.createUser(request);

		if (newUser != null) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else {

			return new ResponseEntity<>("Email is already registered.", HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody final UserLoginRequest request) {
		log.info(request.toString());
		final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

		if (authenticate.isAuthenticated()) {
			final User user = userService.getUserByEmail(request.email());

			if (user == null) {
				return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
			}

			final Claims claims = Jwts
					.claims()
					.add("role", user.getUserRole())
					.subject(request.email())
					.build();

			return new ResponseEntity<>(JwtUtil.createToken(claims), HttpStatus.OK);
		}

		return new ResponseEntity<>("Wrong password", HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/test")
	public ResponseEntity<String> test() {
		/* Get current user email by jwt */
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final String username = authentication.getName();

		return new ResponseEntity<>(String.format("Hello %s, You are logged in successfully.", userService.getUserByEmail(username).getName()), HttpStatus.OK);
	}

	@GetMapping("/get-user")
	public ResponseEntity<UserObject> getUserObject() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final String username = authentication.getName();

		final User user = userService.getUserByEmail(username);

		return new ResponseEntity<>(new UserObject(user.getName(), user.getEmail(), user.getUserRole()), HttpStatus.OK);
	}
}
