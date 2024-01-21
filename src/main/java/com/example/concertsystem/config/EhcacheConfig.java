package com.example.concertsystem.config;



import com.example.concertsystem.Wrapper.ListWrapper;
import com.example.concertsystem.dto.EventResponse;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@Configuration
public class EhcacheConfig {

    @Bean
    @Primary
    public CacheManager EhcacheManager() {

        CacheConfiguration<String, ListWrapper> cacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class,
                        ListWrapper.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(30, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        javax.cache.configuration.Configuration<String, ListWrapper> configuration = Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfig);
        cacheManager.createCache("eventCacheStore1", configuration);
        return cacheManager;
    }
    @Bean
    public CacheManager EhcacheManager2() {

        CacheConfiguration<String, EventResponse> cacheConfig2 = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class,
                        EventResponse.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .offheap(10, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(10)))
                .build();

        CachingProvider cachingProvider2 = Caching.getCachingProvider();
        CacheManager cacheManager2 = cachingProvider2.getCacheManager();

        javax.cache.configuration.Configuration<String, EventResponse> configuration = Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfig2);
        cacheManager2.createCache("eventCacheStore2", configuration);
        return cacheManager2;
    }


}
