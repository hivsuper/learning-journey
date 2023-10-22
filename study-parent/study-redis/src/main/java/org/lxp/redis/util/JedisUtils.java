package org.lxp.redis.util;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

public class JedisUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JedisUtils.class);
    private final JedisPool jedisPool;
    private final String password = "123456";
    private final int port = 6379;
    private final String host = "127.0.0.1";

    public JedisUtils() {
        JedisPoolConfig config = new JedisPoolConfig();// Jedis池配置
        config.setMaxIdle(1000 * 60);// 对象最大空闲时间
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, host, port, 0, password);
    }

    JedisUtils(JedisPool pool) {
        jedisPool = pool;
    }

    /**
     * 发布一个消息
     * 
     * @param channel
     * @param message
     */
    public void publishMsg(String channel, String message) {
        try (Jedis jedis = getResource()) {
            jedis.publish(channel, message);
            LOG.debug("publishMsg {} = {}", channel, message);
        } catch (Exception e) {
            LOG.error("publishMsg {} = {}", channel, message, e);
        }
    }

    /**
     * 发布一个消息
     * 
     * @param channel
     * @param message
     */
    public void publishMsg(byte[] channel, byte[] message) {
        try (Jedis jedis = getResource()) {
            jedis.publish(channel, message);
            LOG.debug("publishMsg {} = {}", channel, message);
        } catch (Exception e) {
            LOG.error("publishMsg {} = {}", channel, message, e);
        }
    }

    /**
     * 接收消息。在main方法调用后，会一直执行下去。当有发布对应消息时，就会在jedisPubSub中接收到！
     * 
     * @param jedisPubSub
     * @param channels
     */
    public void subscribeMsg(JedisPubSub jedisPubSub, String... channels) {
        try (Jedis jedis = getResource()) {
            jedis.subscribe(jedisPubSub, channels);
            LOG.debug("subscribeMsg {} = {}", jedisPubSub, channels);
        } catch (Exception e) {
            LOG.error("subscribeMsg {} = {}", jedisPubSub, channels, e);
        }
    }

    /**
     * 批量添加sorted-set
     * 
     * @param key
     * @param values
     */
    public void zadd(String key, Map<String, Double> values) {
        try (Jedis jedis = getResource()) {
            for (Entry<String, Double> entry : values.entrySet()) {
                jedis.zadd(key, entry.getValue(), entry.getKey());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public String set(String key, String value) {
        String rtn = null;
        try (Jedis jedis = getResource()) {
            rtn = jedis.set(key, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return rtn;
    }

    public String get(String key) {
        String rtn = null;
        try (Jedis jedis = getResource()) {
            rtn = jedis.get(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return rtn;
    }

    public Set<String> zrange(String key, int start, int end) {
        Set<String> rtn = Collections.emptySet();
        try (Jedis jedis = getResource()) {
            rtn = jedis.zrange(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return rtn;
    }

    public Jedis getResource() {
        return jedisPool.getResource();
    }
}
