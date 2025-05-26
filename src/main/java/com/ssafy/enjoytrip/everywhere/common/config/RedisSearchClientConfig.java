package com.ssafy.enjoytrip.everywhere.common.config;


import io.redisearch.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisSearchClientConfig {

    @Bean
    public Client placeSearchClient(
            @Value("${spring.data.redis.place.index}") String indexName,
            @Value("${spring.data.redis.place.host}") String host,
            @Value("${spring.data.redis.place.port}") int port
    ) {
        return new Client(indexName, host, port);
    }

    @Bean
    public Client userSearchClient(
            @Value("${spring.data.redis.user.index}") String indexName,
            @Value("${spring.data.redis.user.host}") String host,
            @Value("${spring.data.redis.user.port}") int port
    ) {
        return new Client(indexName, host, port);
    }
}