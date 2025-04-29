/*
 * Copyright 2025 LINE Corporation
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

package com.linecorp.bot.client.base.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class EventListenerAdapter extends EventListener {
    private final HttpEventListener httpEventListener;

    public EventListenerAdapter(HttpEventListener httpEventListener) {
        this.httpEventListener = httpEventListener;
    }

    @Override
    public void callStart(Call call) {
        httpEventListener.callStart(new HttpCall(call));
    }

    @Override
    public void proxySelectStart(Call call, HttpUrl url) {
        httpEventListener.proxySelectStart(new HttpCall(call), new HttpEndpoint(url));
    }

    @Override
    public void proxySelectEnd(Call call, HttpUrl url, List<Proxy> proxies) {
        httpEventListener.proxySelectEnd(new HttpCall(call), new HttpEndpoint(url), proxies);
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        httpEventListener.dnsStart(new HttpCall(call), domainName);
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        httpEventListener.dnsEnd(new HttpCall(call), domainName, inetAddressList);
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        httpEventListener.connectStart(new HttpCall(call), inetSocketAddress, proxy);
    }

    @Override
    public void secureConnectStart(Call call) {
        httpEventListener.secureConnectStart(new HttpCall(call));
    }

    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        httpEventListener.secureConnectEnd(new HttpCall(call), handshake != null ? new TlsHandshake(handshake) : null);
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        httpEventListener.connectEnd(new HttpCall(call), inetSocketAddress, proxy, protocol != null ? new HttpProtocol(protocol) : null);
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
        httpEventListener.connectFailed(new HttpCall(call), inetSocketAddress, proxy, protocol != null ? new HttpProtocol(protocol) : null, ioe);
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        httpEventListener.connectionAcquired(new HttpCall(call), new HttpConnection(connection));
    }

    @Override
    public void connectionReleased(Call call, Connection connection) {
        httpEventListener.connectionReleased(new HttpCall(call), new HttpConnection(connection));
    }

    @Override
    public void requestHeadersStart(Call call) {
        httpEventListener.requestHeadersStart(new HttpCall(call));
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        httpEventListener.requestHeadersEnd(new HttpCall(call), new HttpRequest(request));
    }

    @Override
    public void requestBodyStart(Call call) {
        httpEventListener.requestBodyStart(new HttpCall(call));
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        httpEventListener.requestBodyEnd(new HttpCall(call), byteCount);
    }

    @Override
    public void requestFailed(Call call, IOException ioe) {
        httpEventListener.requestFailed(new HttpCall(call), ioe);
    }

    @Override
    public void responseHeadersStart(Call call) {
        httpEventListener.responseHeadersStart(new HttpCall(call));
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        httpEventListener.responseHeadersEnd(new HttpCall(call), new HttpResponse(response));
    }

    @Override
    public void responseBodyStart(Call call) {
        httpEventListener.responseBodyStart(new HttpCall(call));
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        httpEventListener.responseBodyEnd(new HttpCall(call), byteCount);
    }

    @Override
    public void responseFailed(Call call, IOException ioe) {
        httpEventListener.responseFailed(new HttpCall(call), ioe);
    }

    @Override
    public void callEnd(Call call) {
        httpEventListener.callEnd(new HttpCall(call));
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        httpEventListener.callFailed(new HttpCall(call), ioe);
    }

    @Override
    public void canceled(Call call) {
        httpEventListener.canceled(new HttpCall(call));
    }

    @Override
    public void satisfactionFailure(Call call, Response response) {
        httpEventListener.satisfactionFailure(new HttpCall(call), new HttpResponse(response));
    }

    @Override
    public void cacheHit(Call call, Response response) {
        httpEventListener.cacheHit(new HttpCall(call), new HttpResponse(response));
    }

    @Override
    public void cacheMiss(Call call) {
        httpEventListener.cacheMiss(new HttpCall(call));
    }

    @Override
    public void cacheConditionalHit(Call call, Response cachedResponse) {
        httpEventListener.cacheConditionalHit(new HttpCall(call), new HttpResponse(cachedResponse));
    }
}
