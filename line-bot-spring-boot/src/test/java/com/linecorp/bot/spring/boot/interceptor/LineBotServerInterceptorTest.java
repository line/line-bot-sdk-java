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

package com.linecorp.bot.spring.boot.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.linecorp.bot.parser.WebhookParser;

@ExtendWith(MockitoExtension.class)
public class LineBotServerInterceptorTest {
    @InjectMocks
    LineBotServerInterceptor target;

    @Mock
    WebhookParser webhookParser;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Test
    public void preHandleStaticResourceTest() throws Exception {
        ResourceHttpRequestHandler handler = mock(ResourceHttpRequestHandler.class);
        boolean result = target.preHandle(request, response, handler);
        assertThat(result).isTrue();
    }

    @Test
    public void preHandleForNotBotMessageHandler() throws Exception {
        HandlerMethod handler = mock(HandlerMethod.class);
        when(handler.getMethodParameters()).thenReturn(new MethodParameter[] {});

        boolean result = target.preHandle(request, response, handler);
        assertThat(result).isTrue();
    }
}
