package org.lxp.redis.util;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class JedisWithPipelineUtilsTest {
    private final String key = "test_pipeline_sorted_set";
    private JedisUtils jedisUtils;
    private JedisWithPipelineUtils jedisWithPipelineUtils;

    @Before
    public void setUp() {
        jedisUtils = new JedisUtils();
        jedisWithPipelineUtils = new JedisWithPipelineUtils(jedisUtils);
        jedisWithPipelineUtils.del(key);
    }

    @Test
    public void doTest() throws Exception {
        List<String> list = Arrays.asList("1", "2");
        for (String s : list) {
            Assert.assertEquals(1, jedisWithPipelineUtils.zadd(key, 1, s));
        }
        Assert.assertEquals("[1, 2]", jedisWithPipelineUtils.zrange(key, 0, list.size() * 2).toString());
        Assert.assertEquals(2, jedisWithPipelineUtils.zrem(key, list));
    }

    @Test
    public void testZpop() throws Exception {
        List<String> list = Arrays.asList("1", "2");
        int size = list.size() * 2;
        for (String s : list) {
            Assert.assertEquals(1, jedisWithPipelineUtils.zadd(key, 1, s));
        }
        Assert.assertEquals("[1, 2]", jedisWithPipelineUtils.zpop(key, size).toString());
        Assert.assertTrue(jedisWithPipelineUtils.zrange(key, 0, size).isEmpty());
    }

    @Test
    public void testBatchZadd() throws Exception {
        // fake data
        final int size = 10000;
        Map<String, Double> values = new HashMap<>();
        for (int index = 0; index < size; index++) {
            values.put(String.valueOf(index), Double.valueOf(index + 0.1));
        }
        // round 1
        long startTime1 = System.currentTimeMillis();
        jedisWithPipelineUtils.zadd(key, values);
        long costTime1 = System.currentTimeMillis() - startTime1;
        // round 2
        long startTime2 = System.currentTimeMillis();
        jedisUtils.zadd(key, values);
        long costTime2 = System.currentTimeMillis() - startTime2;
        // verify
        System.out.println("costTime1:" + costTime1 + ", costTime2:" + costTime2);
        Assert.assertTrue(costTime2 > costTime1);
    }

    @Test
    public void testDel() throws Exception {
        // given
        jedisUtils = mock(JedisUtils.class);
        jedisWithPipelineUtils = new JedisWithPipelineUtils(jedisUtils);
        Jedis jedis = mock(Jedis.class);
        Pipeline pipeline = mock(Pipeline.class);
        doReturn(jedis).when(jedisUtils).getResource();
        doReturn(pipeline).when(jedis).pipelined();
        Response<Long> response = new Response<>(BuilderFactory.LONG);
        response.set(1L);
        doReturn(response).when(pipeline).del(key);
        // execute and verify
        Assert.assertThat(jedisWithPipelineUtils.del(key), Matchers.is(1L));
    }
}
