package com.cluster.redis.demo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenbo@guworks.cc
 * @title 入门 参考 demo
 * @date 2018年12月20日
 * @since v1.0.0
 */
public class ClusterBuilder extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ClusterBuilder.class);

    public static void createCluster() throws InterruptedException {
        List<ClusterNode> clusterNodes = new ArrayList<>();

        //主节点
        List<ClusterNode> masterNodes = new ArrayList<>();

        //从节点
        List<ClusterNode> slaveNodes = new ArrayList<>();

        String[] endpoints = {"127.0.0.1:6379","127.0.0.1:6380","127.0.0.1:6381","127.0.0.1:6382","127.0.0.1:6383","127.0.0.1:6384"};

        int index = 0;
        for (String endpoint : endpoints) {
            String[] ipAndPort = endpoint.split(":");
            ClusterNode node = new ClusterNode(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
            clusterNodes.add(node);

            if (index < 3) {
                // 将6379，6380，6381设置为主节点，其余为从节点
                masterNodes.add(node);
            } else {
                slaveNodes.add(node);
            }

            index++;
        }

        // 分别与各个Redis节点建立通信连接
        for (ClusterNode node : clusterNodes) {
            RedisURI redisURI = RedisURI.Builder.redis(node.getHost(), node.getPort()).build();

            RedisClient redisClient = RedisClient.create(redisURI);

            try {
                StatefulRedisConnection<String, String> connection = redisClient.connect();
                connection.setTimeout(Duration.ofSeconds(10));

                node.setRedisClient(redisClient);
                node.setConnection(connection);
                node.setMyId(connection.sync().clusterMyId());
            } catch (RedisException e) {
                logger.error("excp,", e);
            }
        }

        //执行cluster meet命令是各个孤立的节点相互感知，初步形成集群。
        //只需以一个节点为基准，让所有节点与之meet即可
        ClusterNode firstNode = null;
        for (ClusterNode node : clusterNodes) {
            if (firstNode == null) {
                firstNode = node;
            } else {
                try {
                    node.getConnection().sync().clusterMeet(firstNode.getHost(), firstNode.getPort());
                } catch (RedisCommandTimeoutException | RedisConnectionException e) {
                    e.printStackTrace();
                }
            }
        }

        //为主节点指派slot,将16384个slot分成三份：5460，5461，5462
        int[] slots = {0,5460,5461,10921,10922,16383};
        index = 0;
        for (ClusterNode node : masterNodes) {
            node.setSlotsBegin(slots[index]);
            index++;
            node.setSlotsEnd(slots[index]);
            index++;
        }

        //通过与各个主节点的连接，执行addSlots命令为主节点指派slot
        System.out.println("Start to set slots...");
        for (ClusterNode node : masterNodes) {
            try {
                node.getConnection().sync().clusterAddSlots(createSlots(node.getSlotsBegin(), node.getSlotsEnd()));
            } catch (RedisCommandTimeoutException | RedisConnectionException e) {
                e.printStackTrace();
                System.out.println("add slots failed-->" + node.getHost() + ":" + node.getPort());
            }
        }

        //延时5s，等待slot指派完成
        sleep(5000);

        //为已经指派slot的主节点设置从节点,6379,6380,6381分别对应6382，6383，6384
        index = 0;
        for (ClusterNode node : slaveNodes) {
            try {
                node.getConnection().sync().clusterReplicate(masterNodes.get(index).getMyId());
                index++;
            } catch (RedisCommandTimeoutException | RedisConnectionException e) {
                e.printStackTrace();
                System.out.println("replicate failed-->" + node.getHost() + ":" + node.getPort());
            }
        }

        //关闭连接
        for (ClusterNode node : clusterNodes) {
            node.getConnection().close();
            node.getRedisClient().shutdown();
        }
    }

    public static int[] createSlots(int from, int to) {
        int[] result = new int[to - from + 1];
        int counter = 0;

        for (int i = from; i <= to; i++) {
            result[counter++] = i;
        }

        return result;
    }

    public static void main(String[] args) {
        try {
            ClusterBuilder.createCluster();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
