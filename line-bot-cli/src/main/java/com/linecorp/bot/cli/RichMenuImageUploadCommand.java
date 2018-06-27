package com.linecorp.bot.cli;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.util.concurrent.Futures.getUnchecked;
import static javax.activation.FileTypeMap.getDefaultFileTypeMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
@ConditionalOnProperty(name = "command", havingValue = "richmenu-upload")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RichMenuImageUploadCommand implements CliCommand {
    private LineMessagingClient lineMessagingClient;
    private Arguments arguments;

    @Override
    public void execute() throws IOException {
        final String richMenuId = checkNotNull(arguments.getRichMenuId(), "--rich-menu-id= is not set.");
        final String image = checkNotNull(arguments.getImage(), "--image= is not set.");
        final String contentType = checkNotNull(getDefaultFileTypeMap().getContentType(image),
                                                "Can't assume Content-Type");
        log.info("Content-Type: {}", contentType);

        byte[] bytes = Files.readAllBytes(Paths.get(image));
        final BotApiResponse botApiResponse =
                getUnchecked(lineMessagingClient.setRichMenuImage(richMenuId, contentType, bytes));

        log.info("Request Successfully finished. {}", botApiResponse);
    }
}
