package com.nowcoder.community;

import com.nowcoder.community.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author gsyzh
 * @create 2020-05-17 11:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ThreadPoolTests {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTests.class);
    Object target;
    //JDK的线程池(5个线程)
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    //JDK可执行定时任务的线程池(5个线程)
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Autowired
    private AlphaService alphaService;

    private void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //1.JDK普通线程池
    @Test
    public void testExecutorService() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("HELLO Executor");
            }
        };
        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }
        sleep(10000);
    }

    //2.JDK可执行定时任务的线程池(5个线程)
    @Test
    public void testScheduled() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("HELLO");

            }
        };
        scheduledExecutorService.scheduleAtFixedRate(task, 10000, 1000, TimeUnit.MILLISECONDS);
        sleep(30000);
    }

    //3.spring的线程池,灵活，可以配置
    @Test
    public void testThreadExecutor() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("HELLO");

            }
        };
        for (int i = 0; i < 10; i++) {
            threadPoolTaskExecutor.submit(task);
        }
        sleep(10000);
    }

    //4.spring定时任务
    @Test
    public void testThreadScheduled() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("HELLO");

            }
        };
        Date date = new Date(System.currentTimeMillis() + 10000);
        threadPoolTaskScheduler.scheduleAtFixedRate(task, date, 1000);
        sleep(30000);
    }

    //5.Async
    @Test
    public void testThreadScheduled5() {
        for (int i = 0; i < 10; i++) {
            alphaService.execute1();
        }
        sleep(10000);
    }

    //6.Async
    @Test
    public void testThreadScheduled6() {
        sleep(30000);
    }
}
