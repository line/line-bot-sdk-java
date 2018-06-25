package com.linecorp.bot.cli;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.linecorp.bot.cli.arguments.Arguments;
import com.linecorp.bot.client.ChannelManagementSyncClient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "command", havingValue = "liff-delete")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LiffDeleteCommand implements CliCommand {
    private ChannelManagementSyncClient channelManagementClient;
    private Arguments arguments;

    @Override
    public void execute() {
        checkNotNull(arguments.getLiffId(), "--liff-id= is not set.");

        try {
            channelManagementClient.deleteLiffApp(arguments.getLiffId());
        } catch (Exception e) {
            log.error("Failed : {}", e.getMessage());
            return;
        }
        log.info("Successfully finished.");
    }
}
