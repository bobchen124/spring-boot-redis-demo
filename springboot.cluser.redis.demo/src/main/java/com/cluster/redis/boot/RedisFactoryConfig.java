package com.cluster.redis.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author chenbo@guworks.cc
 * @title xxx
 * @date 2018年12月20日
 * @since v1.0.0
 */
@Configuration
public class RedisFactoryConfig {

    @Autowired
    ClusterConfigurationProperties clusterConfigurationProperties;

    /**
     *
     * @return
     */
    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory(new RedisClusterConfiguration(clusterConfigurationProperties.getNodes()));
    }

    /**
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisClusterConnection clusterConnection(RedisConnectionFactory redisConnectionFactory) {
        return redisConnectionFactory.getClusterConnection();
    }

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

}
