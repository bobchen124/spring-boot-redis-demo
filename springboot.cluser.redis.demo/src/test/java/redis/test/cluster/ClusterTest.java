package redis.test.cluster;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() {
        //System.out.println(redisClusterConnection.set("t".getBytes(), "tt".getBytes()));

        System.out.println(new String(redisClusterConnection.get("t".getBytes())));
    }

    @Test
    public void strTest() {
        System.out.println(stringRedisTemplate.opsForValue().get("t"));
    }

}
