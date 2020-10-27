package ma.azdad.service;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheService {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CacheManager cacheManager;

	public void evictAllCaches() {
		cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

	public void evictCachePrefix(String prefix) {
		log.info("evictCachePrefix : " + prefix);
		cacheManager.getCacheNames().stream().filter(i -> i.startsWith(prefix)).forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

	public void evictCache(String... names) {
		Arrays.stream(names).forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

}