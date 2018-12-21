package redis.test.cluster;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisClusterConnection;
import redis.test.BaseTest;

/**
 * @author chenbo@guworks.cc
 * @title xxx
 * @date 2018年12月21日
 * @since v1.0.0
 */
public class ClusterTest extends BaseTest {

    @Autowired
    RedisClusterConnection redisClusterConnection;

    @Test
    public void test() {
        //System.out.println(redisClusterConnection.set("t".getBytes(), "tt".getBytes()));

        System.out.println(new String(redisClusterConnection.get("t".getBytes())));
    }

}
