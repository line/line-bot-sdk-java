package com.linecorp.bot.model.message;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FxFontSize;
import com.linecorp.bot.model.message.flex.unit.FxLayout;
import com.linecorp.bot.model.message.flex.unit.FxMarginSize;
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
                     .size(ImageSize.FullWidth)
                     .aspectRatio(ImageAspectRatio.R20to13)
                     .aspectMode(ImageAspectMode.Cover)
                     .action(new URIAction("label", "http://example.com"))
                     .build();

        final Box bodyBlock;
        {
            final Text title =
                    Text.builder().text("Brown Cafe").weight(TextWeight.Bold).size(FxFontSize.Xl).build();

            final Box review;
            {
                final Icon goldStar =
                        Icon.builder().size(FxFontSize.Sm).url("https://example.com/gold_star.png").build();
                final Icon grayStar =
                        Icon.builder().size(FxFontSize.Sm).url("https://example.com/gray_star.png").build();
                final Text point =
                        Text.builder()
                            .text("4.0")
                            .size(FxFontSize.Sm)
                            .color("#999999")
                            .margin(FxMarginSize.Md)
                            .flex(0)
                            .build();

                review = Box.builder()
                            .layout(FxLayout.Baseline)
                            .margin(FxMarginSize.Md)
                            .contents(Arrays.asList(goldStar, goldStar, goldStar, goldStar, grayStar, point))
                            .build();
            }

            final Box info;
            {
                final Box place =
                        Box.builder()
                           .layout(FxLayout.Baseline)
                           .spacing(FxMarginSize.Sm)
                           .contents(
                                   Arrays.asList(
                                           Text.builder()
                                               .text("Place")
                                               .color("#aaaaaa")
                                               .size(FxFontSize.Sm)
                                               .flex(1)
                                               .build(),
                                           Text.builder()
                                               .text("Shinjuku, Tokyo")
                                               .wrap(true)
                                               .color("#666666")
                                               .size(FxFontSize.Sm)
                                               .flex(5)
                                               .build()
                                   )
                           )
                           .build();
                final Box time =
                        Box.builder()
                           .layout(FxLayout.Baseline)
                           .spacing(FxMarginSize.Sm)
                           .contents(
                                   Arrays.asList(
                                           Text.builder()
                                               .text("Time")
                                               .color("#aaaaaa")
                                               .size(FxFontSize.Sm)
                                               .flex(1)
                                               .build(),
                                           Text.builder()
                                               .text("10:00 - 23:00")
                                               .wrap(true)
                                               .color("#666666")
                                               .size(FxFontSize.Sm)
                                               .flex(5)
                                               .build()
                                   )
                           )
                           .build();

                info = Box.builder()
                          .layout(FxLayout.Vertical)
                          .margin(FxMarginSize.Lg)
                          .spacing(FxMarginSize.Sm)
                          .contents(Arrays.asList(place, time))
                          .build();
            }

            bodyBlock = Box.builder()
                      .layout(FxLayout.Vertical)
                      .contents(Arrays.asList(title, review, info))
                      .build();
        }

        final Box footerBlock;
        {
            final Button callAction =
                    Button.builder()
                          .style(ButtonStyle.Link)
                          .height(ButtonHeight.Small)
                          .action(new URIAction("CALL", "tell:000000"))
                          .build();
            final Button websiteAction =
                    Button.builder()
                          .style(ButtonStyle.Link)
                          .height(ButtonHeight.Small)
                          .action(new URIAction("WEBSITE", "http://example.com"))
                          .build();

            footerBlock = Box.builder()
                        .layout(FxLayout.Vertical)
                        .spacing(FxMarginSize.Sm)
                        .contents(Arrays.asList(callAction, websiteAction))
                        .build();
        }

        final Bubble bubble = Bubble.builder().hero(heroBlock).body(bodyBlock).footer(footerBlock).build();
        test(new FlexMessage("ALT", bubble));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    void test(final Message original) {
        final Message reconstructed = serializeThenDeserialize(original);
        assertThat(reconstructed).isEqualTo(original);
    }

    @SneakyThrows
    Message serializeThenDeserialize(final Message original) {
        log.info("Original:      {}", original);
        final String asJson = objectMapper.writeValueAsString(original);
        log.info("AS JSON:       {}", asJson);
        final Message reconstructed = objectMapper.readValue(asJson, Message.class);
        log.info("Reconstructed: {}", reconstructed);

        return reconstructed;
    }
}
