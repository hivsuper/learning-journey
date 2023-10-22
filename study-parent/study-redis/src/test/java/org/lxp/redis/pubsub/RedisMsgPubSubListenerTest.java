package org.lxp.redis.pubsub;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.lxp.redis.util.JedisHelper;
import org.lxp.redis.util.JedisUtils;

/**
 * How to test?</br>
 * 1. Run {@link #testSubscribeMsg()}</br>
 * 2. Run {@link #testPublishMsg()}, message will be sent to #1</br>
 * 3. RUn {@link #testUnsubscribe()}, message will be sent to #1 and it will stop.
 */
@Ignore
public class RedisMsgPubSubListenerTest {
    private static final String NEWS_SHARE_CHANNEL = "news.share";
    private RedisMsgPubSubListener pubsub;
    private JedisUtils jedisUtils;
    private JedisHelper jedisHelper;

    @Before
    public void setUp() {
        pubsub = new RedisMsgPubSubListener();
        jedisUtils = new JedisUtils();
        jedisHelper = new JedisHelper(jedisUtils);
    }

    @Test
    public void testSubscribeMsg() throws InterruptedException {
        jedisUtils.subscribeMsg(pubsub, NEWS_SHARE_CHANNEL);
    }

    @Test
    public void testPublishMsg() {
        jedisUtils.publishMsg(NEWS_SHARE_CHANNEL, "测试sssssssssss#@$%&*()*&");
    }

    @Test
    public void testUnsubscribe() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                jedisHelper.publishMsg(NEWS_SHARE_CHANNEL, "stop");
                countDownLatch.countDown();
            }
        }, "unsubscribe").start();
        countDownLatch.await();
    }
}
