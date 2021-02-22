package com.zhuravishkin.springbootasync.service;

import com.zhuravishkin.springbootasync.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

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
        log.info("Async start");
        User user = null;
        try {
            user = restTemplate.getForObject("http://localhost:8080/template/get", User.class);
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        log.info("Async end");
        return CompletableFuture.completedFuture(user);
    }
}
