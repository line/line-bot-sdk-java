/*
 * Copyright 2018 LINE Corporation
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

package com.linecorp.bot.model.message;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.action.CameraAction;
import com.linecorp.bot.model.action.CameraRollAction;
import com.linecorp.bot.model.action.LocationAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.quickreply.QuickReply;
import com.linecorp.bot.model.message.quickreply.QuickReplyItem;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.testutil.TestUtil;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Message object reconstruction test.
 *
 * <p>This is not a part of SDK SPEC but please check it is expected/unavoidable or not when any test is broken.
 *
 * <p><strong>IMPORTANT</strong>: Message serialization/deserialization by JSON
 * is to be able to create a proof of concept in simple.
 * This test do not intended serialization format stability.
 * Serialized JSON format may be different depending on the version.
 */
@Slf4j
public class MessageJsonReconstructionTest {
    ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = TestUtil.objectMapperWithProductionConfiguration(false);
    }

    @Test
    public void textMessageTest() {
        test(new TextMessage("TEST"));
    }

    @Test
    public void textMessageWithQuickReplyTest() {
        List<QuickReplyItem> items = asList(
                QuickReplyItem.builder()
                              .action(CameraAction.withLabel("Camera Action Label"))
                              .imageUrl(URI.create("https://example.com/image.png"))
                              .build(),
                QuickReplyItem.builder()
                              .action(CameraRollAction.withLabel("Camera Roll Action Label"))
                              .build(),
                QuickReplyItem.builder()
                              .action(LocationAction.withLabel("Location Action"))
                              .build()
        );

        TextMessage target = TextMessage
                .builder()
                .text("TEST")
                .quickReply(QuickReply.items(items))
                .build();

        test(target);
    }

    @Test
    public void stickerMessageTest() {
        test(new StickerMessage("123", "456"));
    }

    @Test
    public void audioMessageTest() {
        test(new AudioMessage("originalUrl", 20));
    }

    @Test
    public void videoMessageTest() {
        test(new VideoMessage("https://example.com/original", "https://example.com/preview"));
    }

    @Test
    public void imagemapMessageTest() {
        test(ImagemapMessage.builder()
                            .baseUrl("baseUrl")
                            .altText("altText")
                            .baseSize(new ImagemapBaseSize(1040, 1040))
                            .actions(emptyList())
                            .build());
    }

    @Test
    public void locationMessageTest() {
        test(LocationMessage.builder()
                            .title("title")
                            .address("address")
                            .longitude(135.0)
                            .latitude(0.0)
                            .build());
    }

    @Test
    public void templateMessageWithCarouselTemplateTest() {
        final PostbackAction postbackAction = new PostbackAction("postback", "data");
        final CarouselColumn carouselColumn =
                new CarouselColumn("thumbnail", "title", "text", singletonList(postbackAction));
        final CarouselTemplate carouselTemplate = new CarouselTemplate(singletonList(carouselColumn));

        test(new TemplateMessage("ALT", carouselTemplate));
    }

    @Test
    public void templateMessageWithConfirmTemplateTest() {
        final ConfirmTemplate confirmTemplate =
                new ConfirmTemplate("text",
                                    new URIAction("label", "http://example.com"),
                                    new MessageAction("label", "text"));
        test(new TemplateMessage("ALT", confirmTemplate));
    }

    @Test
    public void templateMessageWithButtonsTemplateTest() {
        final ButtonsTemplate buttonsTemplate =
                new ButtonsTemplate("https://example.com", "title", "text",
                                    singletonList(new MessageAction("label", "text")));
        test(new TemplateMessage("ALT", buttonsTemplate));
    }

    @Test
    public void flexMessage() {
        final FlexMessage flexMessage = new ExampleFlexMessageSupplier().get();
        test(flexMessage);
    }

    @Test
    public void multicastTest() {
        final Multicast multicast =
                new Multicast(singleton("LINE_ID"), singletonList(new TextMessage("text")));

        test(multicast);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    void test(final Object original) {
        final Object reconstructed = serializeThenDeserialize(original);
        assertThat(reconstructed).isEqualTo(original);
    }

    @SneakyThrows
    Object serializeThenDeserialize(final Object original) {
        log.info("Original:      {}", original);
        final String asJson = objectMapper.writeValueAsString(original);
        log.info("AS JSON:       {}", asJson);
        final Object reconstructed = objectMapper.readValue(asJson, original.getClass());
        log.info("Reconstructed: {}", reconstructed);

        return reconstructed;
    }
}
