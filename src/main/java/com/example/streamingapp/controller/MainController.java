package com.example.streamingapp.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RestController
public class MainController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, world!";
    }

    @GetMapping("/getDate")
    public String getDate(){
        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

        return formatter.format(today);
    }

}
