package com.linecorp.bot.cli;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.ChannelManagementSyncClient;
import com.linecorp.bot.liff.LiffApp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "command", havingValue = "liff-list")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LiffListCommand implements CliCommand {
    private ChannelManagementSyncClient channelManagementClient;

    @Override
    public void execute() {
        final List<LiffApp> allLiffApps;
        try {
            allLiffApps = channelManagementClient.getAllLiffApps().getApps();
        } catch (Exception e) {
            log.error("Failed : {}", e.getMessage());
            return;
        }
        log.info("Successfully finished.");

        log.info("You have {} LIFF apps.", allLiffApps.size());
        allLiffApps.forEach(liffApp -> {
            log.info("{}", liffApp);
        });
    }
}
