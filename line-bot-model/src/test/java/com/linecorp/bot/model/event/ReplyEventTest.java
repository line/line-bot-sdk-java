/*
 * Copyright 2018 LINE Corporation
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

package com.linecorp.bot.model.event;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.util.ClassUtils;

import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReplyEventTest {
    @Test
    public void eventWithReplyTokenShouldBeImplementReplySupportTest() {
        final List<Class<?>> eventClasses = getAllEventClass();
        Preconditions.checkState(!eventClasses.isEmpty(), "Event classes are empty. Maybe scanning bug.");

        log.info("eventClasses = {}", eventClasses);

        for (Class<?> eventClass : eventClasses) {
            final boolean hasReplyTokenMethod = ClassUtils.hasMethod(eventClass, "getReplyToken");

            if (hasReplyTokenMethod) {
                assertThat(ReplyEvent.class)
                        .isAssignableFrom(eventClass);
            }
        }
    }

    private static List<Class<?>> getAllEventClass() {
        ClassPathScanningCandidateComponentProvider scanningProvider =
                new ClassPathScanningCandidateComponentProvider(false);
        scanningProvider
                .addIncludeFilter((metadataReader, metadataReaderFactory) -> true);

        return scanningProvider.findCandidateComponents(Event.class.getPackage().getName())
                               .stream()
                               .map(BeanDefinition::getBeanClassName)
                               .map(className -> {
                                   try {
                                       return (Class<?>) Class.forName(className);
                                   } catch (ClassNotFoundException e) {
                                       throw new RuntimeException(e);
                                   }
                               })
                               .filter(Event.class::isAssignableFrom)
                               .collect(toList());
    }
}
