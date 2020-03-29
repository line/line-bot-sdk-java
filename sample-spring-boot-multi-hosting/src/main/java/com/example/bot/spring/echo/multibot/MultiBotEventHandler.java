package com.example.bot.spring.echo.multibot;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.example.bot.spring.echo.multibot.handler.CallbackContext;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.ReplyEvent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.AllArgsConstructor;

/**
 * Put your all business logic in this class. I.e. coding here or call your logic method from here.
 */
@Component
@AllArgsConstructor
public class MultiBotEventHandler {
    private final LineMessagingClient messagingClient;

    public void accept(final CallbackContext ctx, final Event event) {
        if (event instanceof ReplyEvent) {
            final ReplyEvent replyEvent = (ReplyEvent) event;

            // In this case, just reply event.toString().
            final CompletableFuture<BotApiResponse> responseFuture = messagingClient.replyMessage(
                    new ReplyMessage(
                            replyEvent.getReplyToken(),
                            TextMessage.builder()
                                       .text(event.toString())
                                       .build()
                    )
            );

            // Use CallbackContext#makeContextAware to call client's method in async context.
            responseFuture.thenAccept(response -> {
                ctx.makeContextAware(() -> messagingClient.pushMessage(new PushMessage(
                        event.getSource().getSenderId(),
                        TextMessage.builder()
                                   .text(response.toString())
                                   .build()
                )));
            });
        }
    }
}
