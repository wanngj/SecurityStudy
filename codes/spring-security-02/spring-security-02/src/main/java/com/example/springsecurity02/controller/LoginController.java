package com.example.springsecurity02.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {


    @RequestMapping("/auth/login")
    public String login() {
        System.out.println("hello login");
        return "login";
    }

}
