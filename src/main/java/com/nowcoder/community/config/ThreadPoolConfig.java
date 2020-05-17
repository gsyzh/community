package com.nowcoder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author gsyzh
 * @create 2020-05-17 11:29
 */
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
