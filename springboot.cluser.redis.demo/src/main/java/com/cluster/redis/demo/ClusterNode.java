package com.cluster.redis.demo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Data;

/**
 * @author chenbo@guworks.cc
 * @title xxx
 * @date 2018年12月20日
 * @since v1.0.0
 */
@Data
public class ClusterNode {

    private String host;

    private int port;

    private int slotsBegin;

    private int slotsEnd;

    private String myId;

    private String masterId;

    private StatefulRedisConnection<String, String> connection;

    private RedisClient redisClient;

    public ClusterNode(String host, int port) {
        this.host = host;
        this.port = port;
        this.slotsBegin = 0;
        this.slotsEnd = 0;
        this.myId = null;
        this.masterId = null;
    }

}
