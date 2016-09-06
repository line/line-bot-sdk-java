package com.linecorp.bot.model.message.imagemap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class URIImageMapActionTest {
    @Test
    public void getLinkUri() throws Exception {
        URIImageMapAction imageMapAction = new URIImageMapAction("http://example.com",
                                                                 new ImageMapArea(1, 2, 3, 4));

        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(imageMapAction);
        assertThat(s).contains("\"type\":\"uri\"");
    }

}
