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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.action.CameraAction;
import com.linecorp.bot.model.action.CameraRollAction;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.DatetimePickerAction.OfLocalDate;
import com.linecorp.bot.model.action.DatetimePickerAction.OfLocalDatetime;
import com.linecorp.bot.model.action.DatetimePickerAction.OfLocalTime;
import com.linecorp.bot.model.action.LocationAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.action.URIAction.AltUri;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.quickreply.QuickReply;
import com.linecorp.bot.model.message.quickreply.QuickReplyItem;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.narrowcast.Filter;
import com.linecorp.bot.model.narrowcast.filter.AgeDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.AgeDemographicFilter.Age;
import com.linecorp.bot.model.narrowcast.filter.AppTypeDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.AppTypeDemographicFilter.AppType;
import com.linecorp.bot.model.narrowcast.filter.AreaDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.AreaDemographicFilter.AreaCode;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter.Gender;
import com.linecorp.bot.model.narrowcast.filter.OperatorDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.SubscriptionPeriodDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.SubscriptionPeriodDemographicFilter.SubscriptionPeriod;
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
    private static final ObjectMapper objectMapper =
            TestUtil.objectMapperWithProductionConfiguration(false);

    @Before
    public void setUp() throws Exception {
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
        test(new AudioMessage(URI.create("http://originalUrl"), 20));
    }

    @Test
    public void videoMessageTest() {
        test(new VideoMessage(URI.create("https://example.com/original"),
                              URI.create("https://example.com/preview")));
    }

    @Test
    public void imagemapMessageTest() {
        test(ImagemapMessage.builder()
                            .baseUrl(URI.create("baseUrl"))
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
                new CarouselColumn(URI.create("http://thumbnail"), "title", "text",
                                   singletonList(postbackAction));
        final CarouselTemplate carouselTemplate = new CarouselTemplate(singletonList(carouselColumn));

        test(new TemplateMessage("ALT", carouselTemplate));
    }

    @Test
    public void templateMessageWithConfirmTemplateTest() {
        final ConfirmTemplate confirmTemplate =
                new ConfirmTemplate("text",
                                    new URIAction("label", URI.create("http://example.com"),
                                                  new AltUri(URI.create("http://example.com/desktop"))),
                                    new MessageAction("label", "text"));
        test(new TemplateMessage("ALT", confirmTemplate));
    }

    @Test
    public void templateMessageWithButtonsTemplateTest() {
        final ButtonsTemplate buttonsTemplate =
                new ButtonsTemplate(URI.create("https://example.com"), "title", "text",
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
                new Multicast(singleton("LINE_ID"), singletonList(new TextMessage("text")), true);
        test(multicast);

        final Multicast multicast2 =
                new Multicast(singleton("LINE_ID"), new TextMessage("text"), true);
        test(multicast2);

        final Multicast multicast3 =
                new Multicast(singleton("LINE_ID"), singletonList(new TextMessage("text")));
        test(multicast3);

        final Multicast multicast4 = new Multicast(singleton("LINE_ID"), new TextMessage("text"));
        test(multicast4);
    }

    @Test
    public void broadcast() {
        final Broadcast broadcast = new Broadcast(new TextMessage("text"));
        test(broadcast);

        final Broadcast broadcast2 = new Broadcast(new TextMessage("text"), true);
        test(broadcast2);

        final Broadcast broadcast3 = new Broadcast(singletonList(new TextMessage("text")));
        test(broadcast3);

        final Broadcast broadcast4 = new Broadcast(singletonList(new TextMessage("text")), true);
        test(broadcast4);
    }

    @Test
    public void narrowcast() {
        test(GenderDemographicFilter.builder()
                                    .oneOf(Collections.singletonList(Gender.MALE))
                                    .build());

        final Narrowcast narrowcast = new Narrowcast(
                new TextMessage("text"),
                Filter.builder()
                      .demographic(
                              GenderDemographicFilter
                                      .builder()
                                      .oneOf(Collections.singletonList(Gender.MALE))
                                      .build())
                      .build()
        );
        test(narrowcast);

        test(AgeDemographicFilter.builder()
                                 .gte(Age.AGE_15)
                                 .lt(Age.AGE_25)
                                 .build());
        test(AppTypeDemographicFilter.builder()
                                     .oneOf(Collections.singletonList(AppType.IOS))
                                     .build());
        test(AreaDemographicFilter.builder()
                                  .oneOf(Collections.singletonList(AreaCode.JP_TOKYO))
                                  .build());
        test(SubscriptionPeriodDemographicFilter.builder()
                                                .gte(SubscriptionPeriod.DAY_7)
                                                .lt(SubscriptionPeriod.DAY_30)
                                                .build());
        test(OperatorDemographicFilter
                     .builder()
                     .and(Arrays.asList(AgeDemographicFilter.builder()
                                                            .gte(Age.AGE_15)
                                                            .lt(Age.AGE_25)
                                                            .build(),
                                        AppTypeDemographicFilter.builder()
                                                                .oneOf(Collections.singletonList(AppType.IOS))
                                                                .build()))
                     .build());
    }

    @Test
    public void datetimePickerActionTest() {
        final DatetimePickerAction<LocalDate> datePickerAction =
                OfLocalDate.builder()
                           .label("labal")
                           .data("postback")
                           .initial(LocalDate.of(2017, 9, 8))
                           .min(LocalDate.of(2017, 9, 8))
                           .max(LocalDate.of(2017, 9, 8))
                           .build();

        final DatetimePickerAction<LocalTime> timePickerAction =
                OfLocalTime.builder()
                           .label("labal")
                           .data("postback")
                           .initial(LocalTime.of(14, 55))
                           .max(LocalTime.of(14, 55))
                           .min(LocalTime.of(14, 55))
                           .build();

        final DatetimePickerAction<LocalDateTime> datetimePickerAction =
                OfLocalDatetime.builder()
                               .label("labal")
                               .data("postback")
                               .initial(LocalDateTime.of(2017, 9, 8, 14, 55))
                               .max(LocalDateTime.of(2017, 9, 8, 14, 55))
                               .min(LocalDateTime.of(2017, 9, 8, 14, 55))
                               .build();

        final ButtonsTemplate buttonsTemplate =
                new ButtonsTemplate(URI.create("https://example.com"), "title", "text",
                                    ImmutableList.of(datePickerAction, timePickerAction, datetimePickerAction));

        test(new TemplateMessage("ALT_TEXT", buttonsTemplate));
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
