package com.simple.redis.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author chenbo@guworks.cc
 * @title xxx
 * @date 2018年09月25日
 * @since v1.0.0
 */
@Service
public class BaseService {

    final Logger logger = LoggerFactory.getLogger(BaseService.class);

    public void reciveMsg(String msg) {
        logger.info("msg = {}", msg);
    }

}
