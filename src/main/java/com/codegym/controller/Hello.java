package com.codegym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Hello {
    @GetMapping
    public String show(){
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
}
