package dev.tehsteel.tbooks.book.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheService {

	private final CacheManager cacheManager;

	public void insertValue(final String cacheName, final String key, final Object value) {
		final Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			cache.put(key, value);
		} else {
			log.debug("Cache not found: {}", cacheName);
			throw new RuntimeException("Cache not found: " + cacheName);
		}
	}

	public <T> T getCachedData(final String cacheName, final String key, final Class<T> clazz) {
		final Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			log.debug("Cache pool not found: {}", cacheName);
			return null;
		}

		if (cache.get(key) == null) {
			log.debug("Cached data not found: {}", key);
			return null;
		}


		return cache.get(key, clazz);
	}

}
