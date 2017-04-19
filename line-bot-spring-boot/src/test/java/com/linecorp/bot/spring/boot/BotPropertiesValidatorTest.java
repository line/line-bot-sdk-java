/*
 * Copyright 2016 LINE Corporation
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

package com.linecorp.bot.spring.boot;

import static com.linecorp.bot.spring.boot.LineBotProperties.ChannelTokenSupplyMode.SUPPLIER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.engine.path.PathImpl.createPathFromString;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class BotPropertiesValidatorTest {
    private static Validator VALIDATOR;

    @BeforeClass
    public static void setUpClass() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        VALIDATOR = factory.getValidator();
    }

    @Test
    public void okForFixedTest() {
        // Do
        Set<ConstraintViolation<LineBotProperties>> constraintViolations =
                VALIDATOR.validate(new LineBotProperties() {{
                    setChannelSecret("SECRET");
                    setChannelToken("TOKEN");
                }});

        //Verify
        assertThat(constraintViolations).isEmpty();
    }

    @Test
    public void ngForFixedTest() {
        // Do
        Set<ConstraintViolation<LineBotProperties>> constraintViolations =
                VALIDATOR.validate(new LineBotProperties() {{
                    setChannelSecret("SECRET");
                }});

        //Verify
        assertThat(constraintViolations)
                .isNotEmpty()
                .filteredOn("propertyPath", createPathFromString("channelToken"))
                .hasOnlyOneElementSatisfying(violation -> {
                    assertThat(violation.getMessage()).isEqualTo("channelToken is null");
                });
    }

    @Test
    public void okForSupplierTest() {
        // Do
        Set<ConstraintViolation<LineBotProperties>> constraintViolations =
                VALIDATOR.validate(new LineBotProperties() {{
                    setChannelTokenSupplyMode(SUPPLIER);
                    setChannelSecret("SECRET");
                }});

        //Verify
        assertThat(constraintViolations).isEmpty();
    }

    @Test
    public void ngForSupplierTest() {
        // Do
        Set<ConstraintViolation<LineBotProperties>> constraintViolations =
                VALIDATOR.validate(new LineBotProperties() {{
                    setChannelTokenSupplyMode(SUPPLIER);
                    setChannelSecret("SECRET");
                    setChannelToken("TOKEN");
                }});

        //Verify
        assertThat(constraintViolations)
                .isNotEmpty()
                .filteredOn("propertyPath", createPathFromString("channelToken"))
                .hasOnlyOneElementSatisfying(violation -> {
                    assertThat(violation.getMessage())
                            .isEqualTo("channelToken should be null if channelTokenSupplyMode = SUPPLIER");
                });
    }
}
