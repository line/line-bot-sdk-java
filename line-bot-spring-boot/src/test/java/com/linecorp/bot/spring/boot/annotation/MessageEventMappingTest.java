package com.linecorp.bot.spring.boot.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;

public class MessageEventMappingTest {
    @MessageEventMapping(TextMessageContent.class)
    public void annotatedMethod() {
    }

    @Test
    public void testAttributePropagationToMetaAnnotation() throws Exception {
        final Method annotatedMethod = getClass().getMethod("annotatedMethod");
        final EventMapping mergedAnnotation =
                AnnotatedElementUtils.getMergedAnnotation(annotatedMethod, EventMapping.class);

        assertThat(mergedAnnotation.value()).isEqualTo(MessageEvent.class);
        assertThat(mergedAnnotation.message()).containsExactly(TextMessageContent.class);
        assertThat(mergedAnnotation.priority()).isEqualTo(MessageEventMapping.PRIORITY);
    }
}
