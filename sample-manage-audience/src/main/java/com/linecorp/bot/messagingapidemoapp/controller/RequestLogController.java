package com.linecorp.bot.messagingapidemoapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.linecorp.bot.messagingapidemoapp.configuration.LineBotConfiguration.ApiCallLog;
import com.linecorp.bot.messagingapidemoapp.configuration.LineBotConfiguration.MyInterceptor;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RequestLogController {
    MyInterceptor myInterceptor;

    @GetMapping("/request_log/")
    public String index(Model model) {
        List<ApiCallLog> logs = new ArrayList<>(myInterceptor.getLogs());
        Collections.reverse(logs);
        model.addAttribute("logs", logs);
        return "request_log/index";
    }

}
