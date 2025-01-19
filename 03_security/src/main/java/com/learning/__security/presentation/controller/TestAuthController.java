package com.learning.__security.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class TestAuthController {

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("hello-secured")
    public String helloSecured() {
        return "Hello Secured";
    }

    @GetMapping("hello-secured2")
    public String helloSecured2() {
        return "Hello Secured2";
    }

}
