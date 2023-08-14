package com.baizhi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        System.out.println("test ...");
        return "test ok!";
    }
    @GetMapping("/test2")
    private void test2() {
        long l = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {

            System.out.println("test2 ..."+i);
        }
        long l1 = System.currentTimeMillis();
        System.out.println(l1-l);
        System.out.println(Thread.currentThread().getName());
    }
}
