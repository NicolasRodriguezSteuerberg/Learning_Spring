package com.learning.__security.presentation.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth2")
@PreAuthorize("denyAll()") // authorizacion por defecto
public class TestAuthController2 {

    @GetMapping("hello")
    @PreAuthorize("permitAll()")
    public String hello() {
        return "hello";
    }

    @GetMapping("hello-secured")
    @PreAuthorize("hasAuthority('READ')")
    public String helloSecured() {
        return "Hello Secured";
    }

    @GetMapping("hello-secured2")
    @PreAuthorize("hasAuthority('CREATE')")
    public String helloSecured2() {
        return "Hello Secured2";
    }

}
