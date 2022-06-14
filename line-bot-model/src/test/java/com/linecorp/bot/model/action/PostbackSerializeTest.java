package com.linecorp.bot.model.action;

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class PostbackSerializeTest {
    private final static ObjectMapper OBJECT_MAPPER = ModelObjectMapper.createNewObjectMapper();

    @SneakyThrows
    private JsonNode readResource(String name) {
        return OBJECT_MAPPER.readTree(getSystemResourceAsStream(name));
    }

    @Test
    public void testPostbackRichMenu() {
        final PostbackAction action = new PostbackAction(
                "LABEL",
                "DATA",
                "DISPLAYTEXT",
                null,
                PostbackAction.InputOptionType.OPEN_RICH_MENU,
                null
        );

        final JsonNode node1 = OBJECT_MAPPER.valueToTree(action);
        final JsonNode node2 = readResource("action/postback_richmenu.json");

        assertEquals(node1, node2);
    }

    @Test
    public void testPostbackKeyboard() {
        final PostbackAction action = new PostbackAction(
                "LABEL",
                "DATA",
                "DISPLAYTEXT",
                null,
                PostbackAction.InputOptionType.OPEN_KEYBOARD,
                "FILLINTEXT"
        );

        final JsonNode node1 = OBJECT_MAPPER.valueToTree(action);
        final JsonNode node2 = readResource("action/postback_keyboard.json");

        assertEquals(node1, node2);
    }
}
