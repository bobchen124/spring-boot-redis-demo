package com.simple.redis.mq;

import com.simple.redis.basic.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author chenbo@guworks.cc
 * @title xxx
 * @date 2018年09月25日
 * @since v1.0.0
 */
@Component
public class RedisMsgSubscriber implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RedisMsgSubscriber.class);

    @Autowired
    BaseService baseService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        logger.info("Message received: {}, channel = {}", message.toString(), new String(message.getChannel()));

        String channel = new String(message.getChannel());

        baseService.reciveMsg(message.toString());
    }

}
