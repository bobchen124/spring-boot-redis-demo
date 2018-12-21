package com.cluster.redis.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenbo@guworks.cc
 * @title Sample RedisConnectionFactory Configuration for Redis Cluster
 * @date 2018年12月21日
 * @since v1.0.0
 */
@Component
@ConfigurationProperties("spring.redis.cluster")
public class ClusterConfigurationProperties {

    List<String> nodes;

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

}
