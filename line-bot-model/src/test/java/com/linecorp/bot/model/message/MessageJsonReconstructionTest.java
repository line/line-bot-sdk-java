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

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.Icon;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectMode;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectRatio;
import com.linecorp.bot.model.message.flex.component.Image.ImageSize;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Spacer;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
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
 * <p><strong>IMPORTANT</strong>: Message serialization/deserialization by JSON is to be able to create a proof of concept in simple.
 * This test do not intended serialization format stability. Serialized JSON format may be different depending on the version.
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
        test(new ImagemapMessage("baseUrl", "altText", new ImagemapBaseSize(1040, 1040),
                                 emptyList()));
    }

    @Test
    public void locationMessageTest() {
        test(new LocationMessage("title", "address", 135.0, 0.0));
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
        final Image heroBlock =
                Image.builder()
                     .url("https://example.com/cafe.jpg")
                     .size(ImageSize.FULL_WIDTH)
                     .aspectRatio(ImageAspectRatio.R20TO13)
                     .aspectMode(ImageAspectMode.Cover)
                     .action(new URIAction("label", "http://example.com"))
                     .build();

        final Box bodyBlock;
        {
            final Text title =
                    Text.builder().text("Brown Cafe").weight(TextWeight.BOLD).size(FlexFontSize.XL).build();

            final Box review;
            {
                final Icon goldStar =
                        Icon.builder().size(FlexFontSize.SM).url("https://example.com/gold_star.png").build();
                final Icon grayStar =
                        Icon.builder().size(FlexFontSize.SM).url("https://example.com/gray_star.png").build();
                final Text point =
                        Text.builder()
                            .text("4.0")
                            .size(FlexFontSize.SM)
                            .color("#999999")
                            .margin(FlexMarginSize.MD)
                            .flex(0)
                            .build();

                review = Box.builder()
                            .layout(FlexLayout.BASELINE)
                            .margin(FlexMarginSize.MD)
                            .contents(Arrays.asList(goldStar, goldStar, goldStar, goldStar, grayStar, point))
                            .build();
            }

            final Box info;
            {
                final Box place =
                        Box.builder()
                           .layout(FlexLayout.BASELINE)
                           .spacing(FlexMarginSize.SM)
                           .contents(
                                   Arrays.asList(
                                           Text.builder()
                                               .text("Place")
                                               .color("#aaaaaa")
                                               .size(FlexFontSize.SM)
                                               .flex(1)
                                               .build(),
                                           Text.builder()
                                               .text("Shinjuku, Tokyo")
                                               .wrap(true)
                                               .color("#666666")
                                               .size(FlexFontSize.SM)
                                               .flex(5)
                                               .build()
                                   )
                           )
                           .build();
                final Box time =
                        Box.builder()
                           .layout(FlexLayout.BASELINE)
                           .spacing(FlexMarginSize.SM)
                           .contents(
                                   Arrays.asList(
                                           Text.builder()
                                               .text("Time")
                                               .color("#aaaaaa")
                                               .size(FlexFontSize.SM)
                                               .flex(1)
                                               .build(),
                                           Text.builder()
                                               .text("10:00 - 23:00")
                                               .wrap(true)
                                               .color("#666666")
                                               .size(FlexFontSize.SM)
                                               .flex(5)
                                               .build()
                                   )
                           )
                           .build();

                info = Box.builder()
                          .layout(FlexLayout.VERTICAL)
                          .margin(FlexMarginSize.LG)
                          .spacing(FlexMarginSize.SM)
                          .contents(Arrays.asList(place, time))
                          .build();
            }

            bodyBlock = Box.builder()
                      .layout(FlexLayout.VERTICAL)
                      .contents(Arrays.asList(title, review, info))
                      .build();
        }

        final Box footerBlock;
        {
            final Spacer spacer = Spacer.builder().size(FlexMarginSize.SM).build();
            final Button callAction =
                    Button.builder()
                          .style(ButtonStyle.LINK)
                          .height(ButtonHeight.SMALL)
                          .action(new URIAction("CALL", "tel:000000"))
                          .build();
            final Separator separator = Separator.builder().build();
            final Button websiteAction =
                    Button.builder()
                          .style(ButtonStyle.LINK)
                          .height(ButtonHeight.SMALL)
                          .action(new URIAction("WEBSITE", "https://example.com"))
                          .build();

            footerBlock = Box.builder()
                        .layout(FlexLayout.VERTICAL)
                        .spacing(FlexMarginSize.SM)
                        .contents(Arrays.asList(spacer, callAction, separator, websiteAction))
                        .build();
        }

        final Bubble bubble = Bubble.builder().hero(heroBlock).body(bodyBlock).footer(footerBlock).build();
        test(new FlexMessage("ALT", bubble));
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
