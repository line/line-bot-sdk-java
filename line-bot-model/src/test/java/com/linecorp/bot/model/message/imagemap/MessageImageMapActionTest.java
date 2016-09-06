package com.linecorp.bot.model.message.imagemap;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageImageMapActionTest {
    @Test
    public void getText() throws Exception {
        MessageImageMapAction imageMapAction = new MessageImageMapAction("hoge", new ImageMapArea(1, 2, 3, 4));

        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(imageMapAction);
        assertThat(s).contains("\"type\":\"message\"");
    }

}
