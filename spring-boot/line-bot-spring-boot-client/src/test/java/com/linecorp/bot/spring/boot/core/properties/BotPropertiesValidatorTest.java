/*
 * Copyright 2023 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.bot.spring.boot.core.properties;

import static com.linecorp.bot.spring.boot.core.properties.LineBotProperties.ChannelTokenSupplyMode.FIXED;
import static com.linecorp.bot.spring.boot.core.properties.LineBotProperties.ChannelTokenSupplyMode.SUPPLIER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.engine.path.PathImpl.createPathFromString;

import java.net.URI;
import java.time.Duration;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BotPropertiesValidatorTest {
    private static Validator VALIDATOR;

    @BeforeAll
    public static void setUpClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        VALIDATOR = factory.getValidator();
    }

    private LineBotProperties newLineBotProperties(
            LineBotProperties.ChannelTokenSupplyMode mode,
            String channelToken
    ) {
        return new LineBotProperties(
                mode,
                channelToken,
                "SECRET",
                URI.create("https://api.line.me/"),
                URI.create("https://api-data.line.me/"),
                URI.create("https://manager.line.biz/"),
                Duration.ofSeconds(10),
                Duration.ofSeconds(10),
                Duration.ofSeconds(10)
        );
    }

    @Test
    public void okForFixedTest() {
        // Do
        Set<ConstraintViolation<LineBotProperties>> constraintViolations =
                VALIDATOR.validate(newLineBotProperties(FIXED, "TOKEN"));

        //Verify
        assertThat(constraintViolations).isEmpty();
    }

    @Test
    public void ngForFixedTest() {
        // Do
        Set<ConstraintViolation<LineBotProperties>> constraintViolations =
                VALIDATOR.validate(newLineBotProperties(FIXED, null));

        //Verify
        assertThat(constraintViolations)
                .isNotEmpty()
                .filteredOn("propertyPath", createPathFromString("channelToken"))
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("channelToken is null");
    }

    @Test
    public void okForSupplierTest() {
        // Do
        Set<ConstraintViolation<LineBotProperties>> constraintViolations =
                VALIDATOR.validate(newLineBotProperties(SUPPLIER, null));

        //Verify
        assertThat(constraintViolations).isEmpty();
    }

    @Test
    public void ngForSupplierTest() {
        // Do
        Set<ConstraintViolation<LineBotProperties>> constraintViolations =
                VALIDATOR.validate(newLineBotProperties(SUPPLIER, "TOKEN"));

        //Verify
        assertThat(constraintViolations)
                .isNotEmpty()
                .filteredOn("propertyPath", createPathFromString("channelToken"))
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("channelToken should be null if channelTokenSupplyMode = SUPPLIER");
    }
}
