package com.linecorp.bot.model.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.util.ClassUtils;

import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReplySupportTest {
    @Test
    @SuppressWarnings("unchecked")
    public void eventWithReplyTokenShouldBeImplementReplySupportTest() {
        ClassPathScanningCandidateComponentProvider scanningProvider =
                new ClassPathScanningCandidateComponentProvider(false);
        scanningProvider
                .addIncludeFilter((metadataReader, metadataReaderFactory) -> true);

        final Set<Class<?>> eventClasses =
                scanningProvider.findCandidateComponents(Event.class.getPackage().getName())
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
                                .collect(Collectors.toSet());

        log.info("eventClasses = {}", eventClasses);

        Preconditions.checkState(!eventClasses.isEmpty(),
                                 "Event classes scan result are empty Scanning bug.");

        for (Class<?> eventClass : eventClasses) {
            final boolean hasReplyTokenMethod = ClassUtils.hasMethod(eventClass, "getReplyToken");

            if (hasReplyTokenMethod) {
                assertThat(Event.ReplySupport.class)
                        .isAssignableFrom(eventClass);
            }
        }
    }
}
