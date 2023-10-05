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

package com.linecorp.bot.moduleattach.client;
import java.net.URI;

import java.util.concurrent.CompletableFuture;

import com.linecorp.bot.client.base.ApiAuthenticatedClientBuilder;
import com.linecorp.bot.client.base.ApiClientBuilder;
import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.client.base.UploadFile;
import com.linecorp.bot.client.base.channel.ChannelTokenSupplier;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.MultipartBody;

import com.linecorp.bot.moduleattach.model.AttachModuleResponse;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public interface LineModuleAttachClient {

        /**
        * 
        * Attach by operation of the module channel provider
            * @param grantType authorization_code (optional)
            * @param code Authorization code received from the LINE Platform. (optional)
            * @param redirectUri Specify the redirect_uri specified in the URL for authentication and authorization. (optional)
            * @param codeVerifier Specify when using PKCE (Proof Key for Code Exchange) defined in the OAuth 2.0 extension specification as a countermeasure against authorization code interception attacks. (optional)
            * @param clientId Instead of using Authorization header, you can use this parameter to specify the channel ID of the module channel. You can find the channel ID of the module channel in the LINE Developers Console.  (optional)
            * @param clientSecret Instead of using Authorization header, you can use this parameter to specify the channel secret of the module channel. You can find the channel secret of the module channel in the LINE Developers Console.  (optional)
            * @param region If you specified a value for region in the URL for authentication and authorization, specify the same value.  (optional)
            * @param basicSearchId If you specified a value for basic_search_id in the URL for authentication and authorization, specify the same value. (optional)
            * @param scope If you specified a value for scope in the URL for authentication and authorization, specify the same value. (optional)
            * @param brandType If you specified a value for brand_type in the URL for authentication and authorization, specify the same value. (optional)
    * 
     * @see <a href="https://developers.line.biz/en/reference/partner-docs/#link-attach-by-operation-module-channel-provider"> Documentation</a>
    */
    
    
    @POST("/module/auth/v1/token")
    @FormUrlEncoded
        
    
    
    
    
    
    
    
    
    
    
    CompletableFuture<Result<AttachModuleResponse>> attachModule(@Field("grant_type") String grantType, @Field("code") String code, @Field("redirect_uri") String redirectUri, @Field("code_verifier") String codeVerifier, @Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("region") String region, @Field("basic_search_id") String basicSearchId, @Field("scope") String scope, @Field("brand_type") String brandType
    );




    public static ApiAuthenticatedClientBuilder<LineModuleAttachClient> builder(String channelToken) {
        return new ApiAuthenticatedClientBuilder<>(URI.create("https://manager.line.biz"), LineModuleAttachClient.class, new LineModuleAttachExceptionBuilder(), channelToken);
    }
    public static ApiAuthenticatedClientBuilder<LineModuleAttachClient> builder(ChannelTokenSupplier channelTokenSupplier) {
        return new ApiAuthenticatedClientBuilder<>(URI.create("https://manager.line.biz"), LineModuleAttachClient.class, new LineModuleAttachExceptionBuilder(), channelTokenSupplier);
    }


}
