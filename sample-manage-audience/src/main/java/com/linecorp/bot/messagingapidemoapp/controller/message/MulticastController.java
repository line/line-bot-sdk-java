package com.linecorp.bot.messagingapidemoapp.controller.message;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.response.BotApiResponse;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class MulticastController {
    private final LineMessagingClient client;
    private final MessageHelper messageHelper;

    @GetMapping("/message/multicast")
    public String multicast() {
        return "message/multicast/form";
    }

    @PostMapping("/message/multicast")
    public RedirectView postMulticast(@RequestParam String to,
                                      @RequestParam String[] messages,
                                      @RequestParam Boolean notificationDisabled)
            throws ExecutionException, InterruptedException {
        Set<String> toList = Arrays.stream(to.split("\r?\n"))
                                   .filter(StringUtils::isNotBlank)
                                   .collect(Collectors.toSet());

        List<Message> messageList = messageHelper.buildMessages(messages);
        BotApiResponse botApiResponse = client.multicast(
                new Multicast(toList, messageList, notificationDisabled))
                                              .get();
        return new RedirectView("/message/multicast/" + botApiResponse.getRequestId());
    }

    @GetMapping("/message/multicast/{requestId}")
    public String result(Model model, @PathVariable String requestId) {
        model.addAttribute("requestId", requestId);
        return "message/multicast/result";
    }
}
