package com.linecorp.bot.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import okhttp3.Interceptor;
import okhttp3.mockwebserver.RecordedRequest;

public class LineMessagingClientBuilderTest extends AbstractWiremockTest {
    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    LineMessagingService lineMessagingServiceMock;

    @Mock
    LineMessagingServiceBuilder delegateMock;

    LineMessagingClientBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = LineMessagingClient.builder("FIXED_TOKEN");

        Whitebox.setInternalState(builder, "delegate", delegateMock);
    }

    @Test
    public void setterTestForApiEndPoint() {
        // Do
        builder.apiEndPoint("https://example.com");

        // Verify
        verify(delegateMock, only()).apiEndPoint("https://example.com");
    }

    @Test
    public void setterTestForConnectTimeout() {
        // Do
        builder.connectTimeout(1234);

        // Verify
        verify(delegateMock, only()).connectTimeout(1234);
    }

    @Test
    public void setterTestForReadTimeout() {
        // Do
        builder.readTimeout(1234);

        // Verify
        verify(delegateMock, only()).readTimeout(1234);
    }

    @Test
    public void setterTestForWriteTimeout() {
        // Do
        builder.writeTimeout(1234);

        // Verify
        verify(delegateMock, only()).writeTimeout(1234);
    }

    @Test
    public void setterTestForAddInterceptorTimeout() {
        final Interceptor mock = mock(Interceptor.class);

        // Do
        builder.addInterceptor(mock);

        // Verify
        verify(delegateMock, only()).addInterceptor(mock);
    }

    @Test
    public void setterTestForAddInterceptorFirst() {
        final Interceptor mock = mock(Interceptor.class);

        // Do
        builder.addInterceptorFirst(mock);

        // Verify
        verify(delegateMock, only()).addInterceptorFirst(mock);
    }

    @Test
    public void setterTestForRemoveAllInterceptors() {
        // Do
        builder.removeAllInterceptors();

        // Verify
        verify(delegateMock, only()).removeAllInterceptors();
    }

    @Test
    public void setterTestForOkHttpClientBuilder() {
        // We cant check because Builder is final and can't be mocked.
    }

    @Test
    public void setterTestForRetrofitBuilder() {
        // We cant check because Builder is final and can't be mocked.
    }

    @Test
    public void testBuilderWithChannelTokenSupplier() throws InterruptedException {
        final LineMessagingClient lineMessagingClient =
                LineMessagingClient.builder(() -> "MOCKED_TOKEN")
                                   .apiEndPoint("http://localhost:" + mockWebServer.getPort())
                                   .build();

        // Do
        lineMessagingClient.getProfile("TEST");

        // Verify
        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getHeader("Authorization"))
                .isEqualTo("Bearer MOCKED_TOKEN");
    }

    @Test
    public void testBuild() {
        when(delegateMock.build()).thenReturn(lineMessagingServiceMock);

        // Do
        LineMessagingClient result = builder.build();

        // Verify
        verify(delegateMock, only()).build();
        assertThat(result)
                .isInstanceOf(LineMessagingClientImpl.class)
                .hasFieldOrPropertyWithValue("retrofitImpl", lineMessagingServiceMock);
    }
}
