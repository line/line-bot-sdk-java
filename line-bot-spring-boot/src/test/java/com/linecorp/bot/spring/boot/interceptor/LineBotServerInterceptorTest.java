package com.linecorp.bot.spring.boot.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@SpringBootTest
public class LineBotServerInterceptorTest {
    LineBotServerInterceptor target = new LineBotServerInterceptor();

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
