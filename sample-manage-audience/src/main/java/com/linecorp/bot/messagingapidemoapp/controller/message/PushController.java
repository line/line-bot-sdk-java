package com.linecorp.bot.messagingapidemoapp.controller.message;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class PushController {
    private final LineMessagingClient client;
    private final MessageHelper messageHelper;

    @GetMapping("/message/push")
    public String push() {
        return "message/push/form";
    }

    @PostMapping("/message/push")
    public RedirectView postPush(@RequestParam String to,
                                 @RequestParam String[] messages,
                                 @RequestParam Boolean notificationDisabled)
            throws ExecutionException, InterruptedException {
        List<Message> messageList = messageHelper.buildMessages(messages);
        BotApiResponse botApiResponse = client.pushMessage(
                new PushMessage(to, messageList, notificationDisabled))
                                              .get();
        return new RedirectView("/message/push/" + botApiResponse.getRequestId());
    }

    @GetMapping("/message/push/{requestId}")
    public String result(Model model, @PathVariable String requestId) {
        model.addAttribute("requestId", requestId);
        return "message/push/result";
    }
}
