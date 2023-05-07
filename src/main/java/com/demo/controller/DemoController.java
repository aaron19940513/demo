package com.demo.controller;

import com.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class DemoController {
    @Autowired
    DemoService demoService;

    @PostMapping("/dynamic")
    public void test() {
        demoService.test();
    }
}