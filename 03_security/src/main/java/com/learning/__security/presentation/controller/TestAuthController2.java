package com.learning.__security.presentation.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth2")
@PreAuthorize("denyAll()") // authorizacion por defecto
public class TestAuthController2 {

    @GetMapping("get")
    @PreAuthorize("hasAuthority('READ')")
    public String helloGet() {
        return "Hello World - GET";
    }

    @PostMapping("post")
    @PreAuthorize("hasAuthority('CREATE') or hasAuthority('READ')")
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
    @PreAuthorize("hasAuthority('REFACTOR')")
    public String helloPatch() {
        return "Hello World - Patch";
    }
}
