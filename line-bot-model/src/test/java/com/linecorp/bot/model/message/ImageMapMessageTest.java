package com.linecorp.bot.model.message;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.message.imagemap.ImageMapArea;
import com.linecorp.bot.model.message.imagemap.ImageMapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImageMapAction;

public class ImageMapMessageTest {
    @Test
    public void test() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ImageMapMessage imageMapMessage = new ImageMapMessage(
                "https://example.com", "hoge",
                new ImageMapBaseSize(1040, 1040),
                Collections.singletonList(
                        new MessageImageMapAction("hoge",
                                                  new ImageMapArea(0, 0, 20, 20))));

        String s = objectMapper.writeValueAsString(imageMapMessage);
        assertThat(s).contains("\"type\":\"imagemap\"");
    }
}
