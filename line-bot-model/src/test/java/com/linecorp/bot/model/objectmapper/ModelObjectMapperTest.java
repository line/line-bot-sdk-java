package com.linecorp.bot.model.objectmapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class ModelObjectMapperTest {
    @Test
    public void createdInstanceIsIsolatedTest() {
        final ObjectMapper first = ModelObjectMapper.createNewObjectMapper();
        final ObjectMapper second = ModelObjectMapper.createNewObjectMapper();

        // Precondition
        assertThat(first).isNotEqualTo(second);
        assertThat(first.getPropertyNamingStrategy())
                .isEqualTo(second.getPropertyNamingStrategy())
                .isNull();

        // Do
        first.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        // Verify
        assertThat(first.getPropertyNamingStrategy())
                .isNotEqualTo(second.getPropertyNamingStrategy());
    }
}
