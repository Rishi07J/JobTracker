package com.rishi.jobtracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.jobtracker.dto.ApiResponse;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public ApiResponse hello() {

        return new ApiResponse(
                "Welcome to Smart Job Application Tracker",
                "1.0",
                "running"
        );
    }
}