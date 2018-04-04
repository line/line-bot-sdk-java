package com.linecorp.bot.model.objectmapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ModelObjectMapper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // Register ParameterNamesModule to read parameter name from lombok generated constructor.
            .registerModule(new ParameterNamesModule())
            // Register JSR-310(java.time.temporal.*) module and read number as millsec.
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

    public ObjectMapper createNewObjectMapper() {
        return OBJECT_MAPPER.copy();
    }
}
