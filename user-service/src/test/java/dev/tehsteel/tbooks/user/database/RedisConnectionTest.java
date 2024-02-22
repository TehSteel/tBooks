package dev.tehsteel.tbooks.user.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RedisConnectionTest {

	private static final String KEY_NAME = "test";
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Test
	public void testRedisConnection() {
		final String testKey = "test";
		final String testValue = "value";
		redisTemplate.opsForValue().set(testKey, testValue);

		final String retrievedValue = redisTemplate.opsForValue().get(testKey);
		assertEquals(testValue, retrievedValue, "Redis connection test failed: Could not retrieve expected value from Redis.");
		redisTemplate.delete(testKey);
	}
}