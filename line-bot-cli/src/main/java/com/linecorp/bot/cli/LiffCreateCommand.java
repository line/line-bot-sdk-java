package com.linecorp.bot.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.linecorp.bot.cli.arguments.PayloadProvider;
import com.linecorp.bot.client.ChannelManagementSyncClient;
import com.linecorp.bot.liff.LiffView;
import com.linecorp.bot.liff.request.LiffAppAddRequest;
import com.linecorp.bot.liff.response.LiffAppAddResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "command", havingValue = "liff-create")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LiffCreateCommand implements CliCommand {
    private ChannelManagementSyncClient channelManagementClient;
    private PayloadProvider payloadProvider;

    @Override
    public void execute() {
        final LiffView liffView = payloadProvider.read(LiffView.class);
        log.info("Request View : {}", liffView);

        final LiffAppAddRequest liffAppAddRequest = new LiffAppAddRequest(liffView);

        final LiffAppAddResponse liffAppAddResponse;
        try {
            liffAppAddResponse = channelManagementClient.addLiffApp(liffAppAddRequest);
        } catch (Exception e) {
            log.error("Failed : {}", e.getMessage());
            return;
        }
        log.info("Successfully finished. Response : {}", liffAppAddResponse);
    }
}
