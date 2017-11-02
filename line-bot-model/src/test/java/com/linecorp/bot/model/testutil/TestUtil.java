package com.linecorp.bot.model.testutil;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtil {
    /**
     * ObjectMapper which same configuration for production except failOnUnknownProperties is configurable.
     * @param failOnUnknownProperties for testing, if true, exception thrown when unknown properties found.
     */
    public static ObjectMapper objectMapperWithProductionConfiguration(final boolean failOnUnknownProperties) {
        return ModelObjectMapper
                .createNewObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
    }
}
