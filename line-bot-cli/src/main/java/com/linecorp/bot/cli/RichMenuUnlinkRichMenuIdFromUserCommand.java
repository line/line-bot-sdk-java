package com.linecorp.bot.cli;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.util.concurrent.Futures.getUnchecked;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.linecorp.bot.cli.arguments.Arguments;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "command", havingValue = "richmenu-unlink")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RichMenuUnlinkRichMenuIdFromUserCommand implements CliCommand {
    private LineMessagingClient lineMessagingClient;
    private Arguments arguments;

    @Override
    public void execute() {
        final String userId = checkNotNull(arguments.getUserId(), "--user-id= is not set.");
        final BotApiResponse botApiResponse =
                getUnchecked(lineMessagingClient.unlinkRichMenuIdFromUser(userId));
        log.info("Successfully finished.");
        log.info("response = {}", botApiResponse);
    }
}
