package com.zhuravishkin.springbootasync.service;

import com.zhuravishkin.springbootasync.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class AsyncService {
    private final RestTemplate restTemplate;

    public AsyncService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<User> getAsync() {
        log.info("async fixed start");
        User user = null;
        CompletableFuture<User> userCompletableFuture = getCachedAsync();
        try {
            user = restTemplate.getForObject("http://localhost:8080/template/get", User.class);
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        try {
            log.warn(userCompletableFuture.get().toString());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        log.info("async fixed end");
        return CompletableFuture.completedFuture(user);
    }

    @Async("cachedThreadPoolTaskExecutor")
    public CompletableFuture<User> getCachedAsync() {
        log.info("async cached start");
        User user = null;
        try {
            user = restTemplate.getForObject("http://localhost:8080/template/get", User.class);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        log.info("async cached end");
        return CompletableFuture.completedFuture(user);
    }
}
