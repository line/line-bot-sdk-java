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

package com.linecorp.bot.client.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class BotAwareJacksonConverter extends Converter.Factory {
    private final JacksonConverterFactory jacksonConverterFactory;

    private BotAwareJacksonConverter(ObjectMapper mapper) {
        this.jacksonConverterFactory = JacksonConverterFactory.create(mapper);
    }

    public static Converter.Factory create(ObjectMapper objectMapper) {
        return new BotAwareJacksonConverter(objectMapper);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (type == BlobContent.class) {
            return BlobContent::new;
        }
        return jacksonConverterFactory.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        if (type == UploadFile.class) {
            return this::buildRequestBodyFromUploadFile;
        } else {
            return jacksonConverterFactory.requestBodyConverter(
                    type, parameterAnnotations, methodAnnotations, retrofit
            );
        }
    }

    private RequestBody buildRequestBodyFromUploadFile(Object o) {
        if (o instanceof UploadFile.StringUploadFile uploadFile) {
            return RequestBody.create(uploadFile.src(), MediaType.parse(uploadFile.contentType()));
        } else if (o instanceof UploadFile.FileUploadFile uploadFile) {
            return RequestBody.create(uploadFile.src(), MediaType.parse(uploadFile.contentType()));
        }

        throw new IllegalArgumentException("Unsupported object: " + o);
    }
}
