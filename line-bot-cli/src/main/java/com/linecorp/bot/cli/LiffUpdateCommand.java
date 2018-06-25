package com.linecorp.bot.cli;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.linecorp.bot.cli.arguments.Arguments;
import com.linecorp.bot.cli.arguments.PayloadProvider;
import com.linecorp.bot.client.ChannelManagementSyncClient;
import com.linecorp.bot.liff.LiffView;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "command", havingValue = "liff-update")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LiffUpdateCommand implements CliCommand {
    private ChannelManagementSyncClient channelManagementClient;
    private PayloadProvider payloadProvider;
    private Arguments arguments;

    @Override
    public void execute() {
        checkNotNull(arguments.getLiffId(), "--liff-id= is not set.");
        final LiffView liffView = payloadProvider.read(LiffView.class);

        try {
            channelManagementClient
                    .updateLiffApp(arguments.getLiffId(), liffView);
        } catch (Exception e) {
            log.error("Failed : {}", e.getMessage());
            return;
        }
        log.info("Successfully finished.");
    }
}
