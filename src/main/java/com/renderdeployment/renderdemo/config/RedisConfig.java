package com.renderdeployment.renderdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {
Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        String host = System.getenv("UPSTASH_REDIS_HOST");
        String portStr = System.getenv("UPSTASH_REDIS_PORT");
        String password = System.getenv("UPSTASH_REDIS_PASSWORD");
        if (host == null || portStr == null) {
            // Local fallback
            host = "localhost";
            portStr = "6379";
            password = ""; // no password for local Redis
            config.setHostName(host);
            config.setPort(Integer.parseInt(portStr));
            LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                    .build();
            return new LettuceConnectionFactory(config, clientConfig);
        }else {
            logger.info("inside else:");
            config.setHostName(host);
            config.setPort(Integer.parseInt(portStr));
            if (password != null && !password.isEmpty()) {
                config.setPassword(RedisPassword.of(password));
            }
            // Build Lettuce client configuration
            LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                    .useSsl()  // enable SSL
                    .build();
            return new LettuceConnectionFactory(config, clientConfig);
        }



    }

    @Bean
    public RedisCacheManager cacheManager(LettuceConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration())
                .build();
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(1))
                .disableCachingNullValues();
    }
}
