package com.linecorp.bot.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.linecorp.bot.cli.arguments.PayloadProvider;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "command", havingValue = "message-push")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MessagePushCommand implements CliCommand {
    private final PayloadProvider payloadProvider;
    private final LineMessagingClient lineMessagingClient;

    @Override
    public void execute() throws Exception {
        // Actually this command always use multicast command to support multiple targets.
        final Multicast multicast = payloadProvider.read(Multicast.class);
        try {
            final BotApiResponse botApiResponse = lineMessagingClient.multicast(multicast).get();
            log.info("Successfully finished: {}", botApiResponse);
        } catch (Exception e) {
            log.error("Failed : {}", e.getMessage(), e);
        }
    }
}
