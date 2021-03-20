package com.zhuravishkin.springbootasync.service;

import com.zhuravishkin.springbootasync.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class AsyncService {
    private final RestTemplate restTemplate;
    private final AsyncCachedService asyncCachedService;

    public AsyncService(RestTemplate restTemplate, AsyncCachedService asyncCachedService) {
        this.restTemplate = restTemplate;
        this.asyncCachedService = asyncCachedService;
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<User> getAsync() {
        log.info("async fixed start");
        User user = null;
        CompletableFuture<User> userCompletableFuture = asyncCachedService.getCachedAsync();
        try {
            user = restTemplate.getForObject("http://localhost:8080/server/get", User.class);
            Thread.sleep(5_000);
            log.warn(userCompletableFuture.get().toString());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        log.info("async fixed end");

        return CompletableFuture.completedFuture(user);
    }
}
