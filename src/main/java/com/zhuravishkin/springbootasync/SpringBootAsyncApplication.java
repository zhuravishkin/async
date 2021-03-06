package com.zhuravishkin.springbootasync;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
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
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("FixedLookup-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "cachedThreadPoolTaskExecutor")
    public Executor cachedThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("CachedLookup-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor executorService(final MeterRegistry meterRegistry) {
        return ExecutorServiceMetrics.monitor(
                meterRegistry,
                threadPoolTaskExecutor(),
                "fixed_thread_pool_task_executor",
                Tags.of("key", "value"));
    }

    @Bean
    public Executor cachedExecutorService(final MeterRegistry meterRegistry) {
        return ExecutorServiceMetrics.monitor(
                meterRegistry,
                cachedThreadPoolTaskExecutor(),
                "cached_thread_pool_task_executor",
                Tags.of("key", "value"));
    }
}
