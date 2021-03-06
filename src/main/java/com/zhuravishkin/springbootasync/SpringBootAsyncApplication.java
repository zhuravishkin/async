package com.zhuravishkin.springbootasync;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.spring.async.ThreadPoolTaskExecutorMetrics;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@SpringBootApplication
public class SpringBootAsyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAsyncApplication.class, args);
    }

    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor(final MeterRegistry meterRegistry) {
        ThreadPoolTaskExecutor executor = ThreadPoolTaskExecutorMetrics.monitor(
                meterRegistry,
                "fixed_thread_pool_task_executor",
                Tags.of("key", "value"));
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("FixedLookup-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "cachedThreadPoolTaskExecutor")
    public Executor cachedThreadPoolTaskExecutor(final MeterRegistry meterRegistry) {
        ThreadPoolTaskExecutor executor = ThreadPoolTaskExecutorMetrics.monitor(
                meterRegistry,
                "cached_thread_pool_task_executor",
                Tags.of("key", "value"));
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("CachedLookup-");
        executor.initialize();
        return executor;
    }

}
