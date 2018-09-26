package com.simple.redis.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenbo@guworks.cc
 * @title xxx
 * @date 2018年09月25日
 * @since v1.0.0
 */
@Configuration
public class RedisConfig {

    @Autowired
    RedisMsgSubscriber redisMsgSubscriber;

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory, MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(factory);

        //可以订阅多个
        List<ChannelTopic> topics = new ArrayList<>();
        topics.add(new ChannelTopic("test-channel"));

        container.addMessageListener(adapter, topics);

        return container;
    }

    @Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(redisMsgSubscriber);
    }


}
