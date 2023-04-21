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

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import com.linecorp.bot.spring.boot.core.properties.BotPropertiesValidator.ValidBotProperties;

public class BotPropertiesValidator implements ConstraintValidator<ValidBotProperties, LineBotProperties> {
    @Target({ ElementType.TYPE })
    @Retention(RUNTIME)
    @Documented
    @Constraint(validatedBy = { BotPropertiesValidator.class })
    public @interface ValidBotProperties {
        Class<?>[] groups() default {};

        String message() default "";

        Class<? extends Payload>[] payload() default {};

    }

    @Override
    public boolean isValid(LineBotProperties value, ConstraintValidatorContext context) {
        switch (value.channelTokenSupplyMode()) {
            case FIXED -> {
                if (value.channelToken() != null) {
                    return true;
                } else {
                    context.buildConstraintViolationWithTemplate("channelToken is null")
                            .addPropertyNode("channelToken")
                            .addConstraintViolation();
                    return false;
                }
            }
            case SUPPLIER -> {
                if (value.channelToken() == null) {
                    return true;
                } else {
                    context.buildConstraintViolationWithTemplate(
                                    "channelToken should be null if channelTokenSupplyMode = SUPPLIER")
                            .addPropertyNode("channelToken")
                            .addConstraintViolation();
                    return false;
                }
            }
        }
        throw new IllegalStateException("Not implemented channelTokenSupplyMode.");
    }
}
