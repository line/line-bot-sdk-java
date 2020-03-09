package com.linecorp.bot.messagingapidemoapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RootController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
