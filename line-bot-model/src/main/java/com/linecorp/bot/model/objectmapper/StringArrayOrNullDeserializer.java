package com.linecorp.bot.model.objectmapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Fallback to null if the json field is not string of array.
 */
public class StringArrayOrNullDeserializer extends StdDeserializer<List<String>> {

    private static final long serialVersionUID = 1L;

    public StringArrayOrNullDeserializer() {
        super((Class<?>) null);
    }

    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode rootNode = p.getCodec().readTree(p);
        if (!rootNode.isArray()) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (JsonNode element : rootNode) {
            if (!element.isTextual()) {
                return null;
            }
            result.add(element.textValue());
        }
        return result;
    }
}
