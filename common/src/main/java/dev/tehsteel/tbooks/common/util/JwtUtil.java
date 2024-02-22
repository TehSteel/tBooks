package dev.tehsteel.tbooks.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public final class JwtUtil {

	private static final long TOKEN_EXPIRATION_DURATION = 86400 * 1000; // 1 day in milliseconds
	private static String JWT_TOKEN = "";
	private static SecretKey KEY = null;

	private JwtUtil() {
	}

	public static void setJwtToken(final String token) {
		if (token == null || token.isEmpty()) {
			throw new IllegalArgumentException("JWT token cannot be null or empty.");
		}
		JWT_TOKEN = token;
		loadKey();
	}

	private static void loadKey() {
		KEY = Keys.hmacShaKeyFor(JWT_TOKEN.getBytes());
	}

	public static String createToken(final Claims claims) {
		final Date now = new Date();
		final Date exp = new Date(now.getTime() + TOKEN_EXPIRATION_DURATION);

		return Jwts.builder()
				.claims(claims)
				.subject(claims.getSubject())
				.issuedAt(now)
				.expiration(exp)
				.signWith(KEY)
				.compact();
	}

	/**
	 * Retrieve the claims from a JWT.
	 *
	 * @param token The JWT string from which to retrieve claims.
	 * @return The claims extracted from the JWT.
	 * @throws JwtException If there is an issue parsing or verifying the JWT.
	 */
	public static Claims getClaims(final String token) {
		if (token == null || token.isEmpty()) {
			throw new IllegalArgumentException("JWT token cannot be null or empty.");
		}
		try {
			return Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload();
		} catch (final JwtException e) {
			throw new JwtException("Failed to parse or verify JWT: " + e.getMessage(), e);
		}
	}

	/**
	 * Check whether a JWT has expired.
	 *
	 * @param token The JWT string to check for expiration.
	 * @return true if the JWT has expired, false otherwise.
	 */
	public static boolean isExpired(final String token) {
		try {
			return getClaims(token).getExpiration().before(new Date());
		} catch (final JwtException e) {
			// Handle parsing or verification errors gracefully
			return false;
		}
	}
}
