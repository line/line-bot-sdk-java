package com.linecorp.bot.model.testutil;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtil {
    /**
     * ObjectMapper which same configuration for production except failOnUnknownProperties is configurable.
     * @param failOnUnknownProperties for testing, if true, exception thrown when unknown properties found.
     */
    public static ObjectMapper objectMapperWithProductionConfiguration(final boolean failOnUnknownProperties) {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties)
                // Register ParameterNamesModule to read parameter name from lombok generated constructor.
                .registerModule(new ParameterNamesModule())
                // Register JSR-310(java.time.temporal.*) module and read number as millsec.
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    }
}
