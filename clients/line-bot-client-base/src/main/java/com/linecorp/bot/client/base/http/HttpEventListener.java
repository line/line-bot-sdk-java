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

public abstract class HttpEventListener {
    public void callStart(HttpCall httpCall) {
    }

    public void proxySelectStart(HttpCall httpCall, HttpEndpoint httpEndpoint) {
    }

    public void proxySelectEnd(HttpCall httpCall, HttpEndpoint httpEndpoint, List<Proxy> proxies) {
    }

    public void dnsStart(HttpCall httpCall, String domainName) {
    }

    public void dnsEnd(HttpCall httpCall, String domainName, List<InetAddress> inetAddressList) {
    }

    public void connectStart(HttpCall httpCall, InetSocketAddress inetSocketAddress, Proxy proxy) {
    }

    public void secureConnectStart(HttpCall httpCall) {
    }

    public void secureConnectEnd(HttpCall httpCall, TlsHandshake tlsHandshake) {
    }

    public void connectEnd(HttpCall httpCall, InetSocketAddress inetSocketAddress, Proxy proxy, HttpProtocol httpProtocol) {
    }

    public void connectFailed(HttpCall httpCall, InetSocketAddress inetSocketAddress, Proxy proxy, HttpProtocol httpProtocol, IOException ioe) {
    }

    public void connectionAcquired(HttpCall httpCall, HttpConnection httpConnection) {
    }

    public void connectionReleased(HttpCall httpCall, HttpConnection httpConnection) {
    }

    public void requestHeadersStart(HttpCall httpCall) {
    }

    public void requestHeadersEnd(HttpCall httpCall, HttpRequest httpRequest) {
    }

    public void requestBodyStart(HttpCall httpCall) {
    }

    public void requestBodyEnd(HttpCall httpCall, long byteCount) {
    }

    public void requestFailed(HttpCall httpCall, IOException ioe) {
    }

    public void responseHeadersStart(HttpCall httpCall) {
    }

    public void responseHeadersEnd(HttpCall httpCall, HttpResponse httpResponse) {
    }

    public void responseBodyStart(HttpCall httpCall) {
    }

    public void responseBodyEnd(HttpCall httpCall, long byteCount) {
    }

    public void responseFailed(HttpCall httpCall, IOException ioe) {
    }

    public void callEnd(HttpCall httpCall) {
    }

    public void callFailed(HttpCall httpCall, IOException ioe) {
    }

    public void canceled(HttpCall httpCall) {
    }

    public void satisfactionFailure(HttpCall httpCall, HttpResponse httpResponse) {
    }

    public void cacheHit(HttpCall httpCall, HttpResponse httpResponse) {
    }

    public void cacheMiss(HttpCall httpCall) {
    }

    public void cacheConditionalHit(HttpCall call, HttpResponse cachedResponse) {
    }
}
