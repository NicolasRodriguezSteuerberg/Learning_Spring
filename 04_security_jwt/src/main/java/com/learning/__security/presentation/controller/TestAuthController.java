package com.learning.__security.presentation.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/methods")
public class TestAuthController {

    @GetMapping("get")
    public String helloGet() {
        return "Hello World - GET";
    }

    @PostMapping("post")
    public String helloPost() {
        return "Hello World - Post";
    }

    @PutMapping("put")
    public String helloPut() {
        return "Hello World - Put";
    }

    @DeleteMapping("delete")
    public String helloDelete() {
        return "Hello World - Delete";
    }

    @PatchMapping("patch")
    public String helloPatch() {
        return "Hello World - Patch";
    }
}

