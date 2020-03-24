/*
 * Copyright 2020 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

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
    private final MyInterceptor myInterceptor;

    @GetMapping("/request_log/")
    public String index(Model model) {
        List<ApiCallLog> logs = new ArrayList<>(myInterceptor.getLogs());
        Collections.reverse(logs);
        model.addAttribute("logs", logs);
        return "request_log/index";
    }

}
