package com.zhuravishkin.springbootasync.controller;

import com.zhuravishkin.springbootasync.model.User;
import com.zhuravishkin.springbootasync.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping(value = "async")
public class Controller {
    AsyncService service;

    @Autowired
    public Controller(AsyncService service) {
        this.service = service;
    }

    @GetMapping(value = "/get")
    public ResponseEntity<User> get() {
        CompletableFuture<User> userCompletableFuture = service.getAsync();
        User userFuture = null;
        try {
            userFuture = userCompletableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return new ResponseEntity<>(userFuture, HttpStatus.OK);
    }
}
