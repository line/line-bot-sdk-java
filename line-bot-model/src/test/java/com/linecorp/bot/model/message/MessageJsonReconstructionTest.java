package com.linecorp.bot.model.message;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;

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
        objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule());
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
