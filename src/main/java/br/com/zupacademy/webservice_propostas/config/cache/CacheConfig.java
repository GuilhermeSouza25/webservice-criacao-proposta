package br.com.zupacademy.webservice_propostas.config.cache;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Caffeine;

@Component
@SuppressWarnings("all")
public class CacheConfig {
	
	@Bean
	public Caffeine caffeineConfig() {
	    return Caffeine.newBuilder()
	    		.expireAfterAccess(30, TimeUnit.MINUTES);
	    		
	}
	
	@Bean
	public CacheManager cacheManager(Caffeine caffeine) {
	    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
	    caffeineCacheManager.setCaffeine(caffeine);
	    return caffeineCacheManager;
	}
}
