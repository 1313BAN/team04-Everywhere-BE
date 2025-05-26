package com.ssafy.enjoytrip.everywhere.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisMultiConfig {

    @Bean
    public RedisConnectionFactory placeRedisConnectionFactory(
            @Value("${spring.data.redis.place.host}") String host,
            @Value("${spring.data.redis.place.port}") int port) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, String> placeRedisTemplate(
            @Qualifier("placeRedisConnectionFactory") RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());       // ✅ 문자열 직렬화
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());   // ✅ 문자열 직렬화
        return template;
    }

    // 👉 6380용 설정
    @Bean
    public RedisConnectionFactory userRedisConnectionFactory(
            @Value("${spring.data.redis.user.host}") String host,
            @Value("${spring.data.redis.user.port}") int port) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, String> userRedisTemplate(
            @Qualifier("userRedisConnectionFactory") RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());       // ✅ 문자열 직렬화
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());   // ✅ 문자열 직렬화
        return template;
    }


    // ✅ Spring이 기대하는 기본 이름: redisTemplate
    @Bean
    public RedisTemplate<String, String> redisTemplate(
            @Qualifier("placeRedisConnectionFactory") RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory); // 👉 기본은 placeRedis 사용
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());       // ✅ 문자열 직렬화
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());   // ✅ 문자열 직렬화
        return template;
    }
}
