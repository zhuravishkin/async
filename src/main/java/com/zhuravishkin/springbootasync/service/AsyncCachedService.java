package com.zhuravishkin.springbootasync.service;

import com.zhuravishkin.springbootasync.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AsyncCachedService {
    private final RestTemplate restTemplate;

    public AsyncCachedService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async("cachedThreadPoolTaskExecutor")
    public CompletableFuture<User> getCachedAsync() {
        log.info("async cached start");
        User user = null;
        try {
            user = restTemplate.getForObject("http://localhost:8080/server/get", User.class);
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        log.info("async cached end");

        return CompletableFuture.completedFuture(user);
    }
}
