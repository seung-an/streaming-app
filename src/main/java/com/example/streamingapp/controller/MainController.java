package com.example.streamingapp.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, world!";
    }

    @GetMapping("/healthCheck")
    public ResponseEntity healthCheck() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
