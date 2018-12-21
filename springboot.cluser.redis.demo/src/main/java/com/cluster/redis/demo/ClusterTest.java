package com.cluster.redis.demo;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenbo@guworks.cc
 * @title xxx
 * @date 2018年12月21日
 * @since v1.0.0
 */
public class ClusterTest {

    public static void main(String[] args) {
        List<ClusterNode> clusterNodeList = new ArrayList<>();
        List<RedisURI> redisUriList = new ArrayList<>();
        String[] endpoints = {"127.0.0.1:6379","127.0.0.1:6380","127.0.0.1:6381","127.0.0.1:6382","127.0.0.1:6383","127.0.0.1:6384"};

        for (String endpoint : endpoints) {
            String[] ipAndPort = endpoint.split(":");
            ClusterNode node = new ClusterNode(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
            clusterNodeList.add(node);
        }

        //创建RedisURI
        for (ClusterNode node : clusterNodeList) {
            RedisURI redisUri = RedisURI.Builder.redis(node.getHost(), node.getPort()).build();
            redisUriList.add(redisUri);
        }

        //创建Redis集群客户端，建立连接，执行set，get基本操作
        RedisClusterClient redisClusterClient = RedisClusterClient.create(redisUriList);
        StatefulRedisClusterConnection<String, String> conn = redisClusterClient.connect();

        RedisAdvancedClusterCommands<String, String> cmd = conn.sync();
        System.out.println(cmd.set("key-test", "value-test"));
        System.out.println(cmd.get("key-test"));

        //关闭连接
        conn.close();
        redisClusterClient.shutdown();
    }

}
