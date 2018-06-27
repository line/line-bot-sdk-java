package com.linecorp.bot.cli;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.util.concurrent.Futures.getUnchecked;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.linecorp.bot.cli.arguments.Arguments;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "command", havingValue = "richmenu-getrichmenuidofuser")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RichMenuGetRichMenuIdOfUserCommand implements CliCommand {
    private LineMessagingClient lineMessagingClient;
    private Arguments arguments;

    @Override
    public void execute() {
        final String userId = checkNotNull(arguments.getUserId(), "--user-id= is not set.");
        final RichMenuIdResponse richMenuIdResponse =
                getUnchecked(lineMessagingClient.getRichMenuIdOfUser(userId));
        log.info("Successfully finished.");
        log.info("response = {}", richMenuIdResponse);
    }
}
