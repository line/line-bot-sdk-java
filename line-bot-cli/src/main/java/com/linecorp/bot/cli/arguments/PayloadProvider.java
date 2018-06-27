package com.linecorp.bot.cli.arguments;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES;
import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Lazy
@Data
@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PayloadProvider {
    private static final ObjectMapper OBJECT_MAPPER = ModelObjectMapper
            .createNewObjectMapper()
            .configure(ALLOW_UNQUOTED_FIELD_NAMES, true)
            .configure(ALLOW_COMMENTS, true)
            .configure(ALLOW_SINGLE_QUOTES, true)
            .configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(INDENT_OUTPUT, true);
    private static final Yaml YAML = new Yaml();
    private final PayloadArguments arguments;

    public <T> T read(Class<T> clazz) {
        try {
            return readInternal(clazz);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            if (e instanceof IOException) {
                throw new UncheckedIOException((IOException) e);
            }
            throw new RuntimeException(e);
        }
    }

    <T> T readInternal(Class<T> clazz) throws Exception {
        if (arguments.getData() != null) {
            return OBJECT_MAPPER.readValue(arguments.getData(), clazz);
        }

        if (arguments.getJson() != null) {
            try (FileInputStream is = new FileInputStream(arguments.getJson())) {
                return OBJECT_MAPPER.readValue(is, clazz);
            }
        }

        if (arguments.getYaml() != null) {
            final Object yamlAsObject;
            try (FileInputStream is = new FileInputStream(arguments.getYaml())) {
                yamlAsObject = YAML.load(is);
            }
            log.debug("{}", yamlAsObject);
            return OBJECT_MAPPER.convertValue(yamlAsObject, clazz);
        }

        throw new IllegalArgumentException("--data= or --json= or --yaml is mandatory");
    }
}
