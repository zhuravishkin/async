package com.zhuravishkin.springbootasync.controller;

import com.zhuravishkin.springbootasync.model.User;
import com.zhuravishkin.springbootasync.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "async")
public class Controller {
    AsyncService service;

    @Autowired
    public Controller(AsyncService service) {
        this.service = service;
    }

    @PostMapping(value = "/post")
    public ResponseEntity<User> post(@RequestBody User user) {
        service.getAsync();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
