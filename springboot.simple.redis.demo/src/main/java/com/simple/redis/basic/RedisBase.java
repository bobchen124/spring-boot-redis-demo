package com.simple.redis.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author chenbo@guworks.cc
 * @title xxx
 * @date 2018年09月25日
 * @since v1.0.0
 */
@Component
public class RedisBase {

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 队列发送
     * @param channel
     * @param message
     */
    public void sendMsg(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }

    /**
     * set
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * get
     * @param key
     * @return
     */
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * setNx key不存在时返回 true
     * @param key
     * @param value
     * @return
     */
    public boolean setNx(final String key, final String value)  {
        return (boolean)redisTemplate.execute((RedisCallback) (connection) -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            return connection.setNX(serializer.serialize(key), serializer.serialize(value));
        });
    }

    /**
     * 分布式锁 - set nx px
     * @param key
     * @param value
     * @param seconds
     */
    public boolean setNxPx(String key, String value, long seconds) {
        return (boolean) redisTemplate.execute((RedisCallback) (connection) -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            Expiration expiration = Expiration.seconds(seconds);
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.SET_IF_ABSENT;
            //RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.SET_IF_PRESENT;
            return connection.set(serializer.serialize(key), serializer.serialize(value), expiration, setOption);
        });
    }

    /**
     * 分布式锁--解锁，lua script
     * @param key
     * @param value
     * @return
     */
    public boolean script(String key, String value) {
        // Lua脚本，用于校验并释放锁
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return '0' end";

        DefaultRedisScript<String> defaultRedisScript = new DefaultRedisScript<>(script, String.class);

        String ret = execute(defaultRedisScript, key, value);
        System.out.println("ret = " + ret);

        if ("1".equals(ret)) {
            return true;
        }

        return false;
    }

    public <T> T execute(RedisScript<T> script, String key, String value) {
        return redisTemplate.execute(script, Arrays.asList(key), value);
    }

}
