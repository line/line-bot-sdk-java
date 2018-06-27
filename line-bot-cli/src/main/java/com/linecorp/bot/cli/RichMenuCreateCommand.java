package com.linecorp.bot.cli;

import static com.google.common.util.concurrent.Futures.getUnchecked;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.linecorp.bot.cli.arguments.PayloadProvider;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "command", havingValue = "richmenu-create")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RichMenuCreateCommand implements CliCommand {
    private LineMessagingClient lineMessagingClient;
    private PayloadProvider payloadProvider;

    @Override
    public void execute() {
        final RichMenu richMenu = payloadProvider.read(RichMenu.class);
        final RichMenuIdResponse richMenuIdResponse =
                getUnchecked(lineMessagingClient.createRichMenu(richMenu));
        log.info("Successfully finished. {}", richMenuIdResponse);
    }
}
