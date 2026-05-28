package com.example.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    // Forward SPA routes to index.html for Vue Router HTML5 history mode
    @RequestMapping(value = {"/", "/login", "/register", "/home"})
    public String index() {
        return "forward:/index.html";
    }
}
