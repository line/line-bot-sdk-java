/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.client.CloseableMessageContent;
import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.client.rich.SimpleRichMessageBuilder;
import com.linecorp.bot.model.callback.Message;
import com.linecorp.bot.model.content.AddedAsFriendOperation;
import com.linecorp.bot.model.content.AudioContent;
import com.linecorp.bot.model.content.BlockedOperation;
import com.linecorp.bot.model.content.ContactContent;
import com.linecorp.bot.model.content.Content;
import com.linecorp.bot.model.content.ImageContent;
import com.linecorp.bot.model.content.LocationContent;
import com.linecorp.bot.model.content.LocationContentLocation;
import com.linecorp.bot.model.content.StickerContent;
import com.linecorp.bot.model.content.TextContent;
import com.linecorp.bot.model.content.VideoContent;
import com.linecorp.bot.model.content.metadata.AudioContentMetadata;
import com.linecorp.bot.model.content.metadata.ContactContentMetadata;
import com.linecorp.bot.model.content.metadata.StickerContentMetadata;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.rich.RichMessage;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class KitchenSinkController {
    @Autowired
    private LineBotClient lineBotClient;

    @RequestMapping("/callback")
    public void callback(@NonNull @LineBotMessages List<Message> messages) {
        log.info("Got request: {}", messages);

        for (Message message : messages) {
            this.handleMessage(message);
        }
    }

    private void handleMessage(Message message) {
        Content content = message.getContent();
        if (content instanceof TextContent) {
            handleTextContent((TextContent) content);
        } else if (content instanceof StickerContent) {
            handleSticker((StickerContent) content);
        } else if (content instanceof LocationContent) {
            handleLocation((LocationContent) content);
        } else if (content instanceof ContactContent) {
            handleContact((ContactContent) content);
        } else if (content instanceof AudioContent) {
            handleAudio((AudioContent) content);
        } else if (content instanceof ImageContent) {
            handleImage((ImageContent) content);
        } else if (content instanceof VideoContent) {
            handleVideo((VideoContent) content);
        } else if (content instanceof BlockedOperation) {
            handleBlocked((BlockedOperation) content);
        } else if (content instanceof AddedAsFriendOperation) {
            handleAddedAsFriend((AddedAsFriendOperation) content);
        } else {
            log.info("Received message(Ignored): {}",
                     content);
        }
    }

    private void handleAddedAsFriend(AddedAsFriendOperation content) {
        String mid = content.getMid();
        log.info("User added this account as a friend: {}", mid);
        try {
            lineBotClient.sendText(mid, "Hi! I'm a bot!");
        } catch (LineBotAPIException e) {
            log.error("LINE server returns '{}'(mid: '{}')",
                      e.getMessage(),
                      mid, e);
        }
    }

    private void handleBlocked(BlockedOperation content) {
        String mid = content.getMid();
        log.info("User blocked this account: {}", mid);
    }

    private void handleVideo(VideoContent content) {
        String mid = content.getFrom();
        String messageId = content.getId();
        try {
            try (CloseableMessageContent messageContent = lineBotClient.getMessageContent(messageId);
                 CloseableMessageContent previewMessageContent = lineBotClient.getPreviewMessageContent(
                         messageId)
            ) {
                String path = saveContent("video", messageContent);
                String previewPath = saveContent("video-preview", previewMessageContent);

                lineBotClient.sendVideo(mid, path, previewPath);
            }
        } catch (IOException e) {
            log.error("Cannot save item '{}'(mid: '{}')",
                      e.getMessage(),
                      messageId, e);
        } catch (LineBotAPIException e) {
            log.error("Error in LINE BOT API: '{}'(mid: '{}')",
                      e.getMessage(),
                      messageId, e);
        }
    }

    private void handleImage(ImageContent content) {
        String mid = content.getFrom();
        String messageId = content.getId();
        try {
            try (CloseableMessageContent messageContent = lineBotClient.getMessageContent(messageId);
                 CloseableMessageContent previewMessageContent = lineBotClient.getPreviewMessageContent(
                         messageId)
            ) {
                String path = saveContent("image", messageContent);
                String previewPath = saveContent("image" + "-preview", previewMessageContent);

                lineBotClient.sendImage(mid, path, previewPath);
            }
        } catch (IOException e) {
            log.error("Cannot save item '{}'(mid: '{}')",
                      e.getMessage(),
                      messageId, e);
        } catch (LineBotAPIException e) {
            log.error("Error in LINE BOT API: '{}'(mid: '{}')",
                      e.getMessage(),
                      messageId, e);
        }
    }

    private void handleAudio(AudioContent content) {
        String mid = content.getFrom();
        AudioContentMetadata contentMetadata = content.getContentMetadata();
        String messageId = content.getId();
        try (CloseableMessageContent messageContent = lineBotClient.getMessageContent(messageId)) {
            String path = saveContent("audio", messageContent);
            lineBotClient.sendAudio(mid, path, contentMetadata.getAudlen());
        } catch (IOException e) {
            log.error("Cannot save image '{}'(mid: '{}')",
                      e.getMessage(),
                      messageId, e);
        } catch (LineBotAPIException e) {
            log.error("Error in LINE BOT API: '{}'(mid: '{}')",
                      e.getMessage(),
                      messageId, e);
        }
    }

    private void handleContact(ContactContent content) {
        String mid = content.getFrom();
        ContactContentMetadata contentMetadata = content.getContentMetadata();
        try {
            lineBotClient.sendText(
                    mid, "Received contact info for : " + contentMetadata.getDisplayName()
            );
        } catch (LineBotAPIException e) {
            log.error("LINE server returns '{}'(mid: '{}')",
                      e.getMessage(),
                      mid, e);
        }
    }

    private void handleLocation(LocationContent content) {
        String mid = content.getFrom();
        LocationContentLocation location = content.getLocation();
        try {
            lineBotClient.sendLocation(
                    mid,
                    content.getText(),
                    location.getTitle(),
                    location.getAddress(),
                    location.getLatitude(),
                    location.getLongitude()
            );
        } catch (LineBotAPIException e) {
            log.error("LINE server returns '{}'(mid: '{}')",
                      e.getMessage(),
                      mid, e);
        }
    }

    private void handleSticker(StickerContent content) {
        String mid = content.getFrom();
        StickerContentMetadata contentMetadata = content.getContentMetadata();
        // Bot can send some built-in stickers.
        try {
            lineBotClient.sendSticker(mid, contentMetadata.getStkpkgid(), contentMetadata.getStkid());
        } catch (LineBotAPIException e) {
            log.error("LINE server returns '{}'(mid: '{}')",
                      e.getMessage(),
                      mid, e);
        }
    }

    private void handleTextContent(TextContent content) {
        String mid = content.getFrom();
        String text = content.getText();

        try {
            log.info("Got text message from {}: {}", mid, text);
            switch (text) {
            case "profile":
                UserProfileResponse userProfile = lineBotClient.getUserProfile(Collections.singletonList(mid));
                lineBotClient.sendText(mid, userProfile.toString());
                break;
            case "multi":
                lineBotClient.createMultipleMessageBuilder()
                             .addText("hoge")
                             .addText("fuga")
                             .send(mid);
                break;
            case "rich":
                final RichMessage richMessage =
                        SimpleRichMessageBuilder.create(1040, 1040)
                        .addWebAction(0, 0, 520, 520, "manga", "https://store.line.me/family/manga/en")
                        .addWebAction(520, 0, 520, 520, "music", "https://store.line.me/family/music/en")
                        .addWebAction(0, 520, 520, 520, "play", "https://store.line.me/family/play/en")
                        .addWebAction(520, 520, 520, 520, "fortune", "https://store.line.me/family/uranai/en")
                        .build();

                lineBotClient.sendRichMessage(
                        mid,
                        createUri("/static/rich"),
                        "This is alt text.",
                        richMessage
                );
                break;
            default:
                log.info("Returns echo message {}: {}", mid, text);
                lineBotClient.sendText(
                        mid,
                        text
                );
                break;
            }
        } catch (LineBotAPIException e) {
            log.error("LINE server returns '{}'(mid: '{}', text: '{}')",
                      e.getMessage(),
                      mid,
                      text, e);
        }
    }

    private String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .path(path).build()
                                          .toUriString();
    }

    private String saveContent(String type, CloseableMessageContent messageContent) throws IOException {
        log.info("Got filename: {}", messageContent.getFileName());
        String path = LocalDateTime.now().toString() + '-' + UUID.randomUUID().toString();
        Path tempFile = KitchenSinkApplication.downloadedContentDir.resolve(path);
        tempFile.toFile().deleteOnExit();
        OutputStream outputStream = Files.newOutputStream(tempFile);
        IOUtils.copy(messageContent.getContent(),
                     outputStream);
        log.info("Saved {}: {}", type, tempFile);
        return createUri("/downloaded/" + path);
    }
}
