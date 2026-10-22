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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Supplier;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import tools.jackson.core.JacksonException;
import tools.jackson.core.exc.JacksonIOException;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

public class BotAwareJacksonConverter extends Converter.Factory {
    private static final MediaType JSON = MediaType.parse("application/json; charset=UTF-8");
    private final Supplier<ObjectMapper> objectMapperSupplier;

    private BotAwareJacksonConverter(Supplier<ObjectMapper> objectMapperSupplier) {
        this.objectMapperSupplier = objectMapperSupplier;
    }

    public static Converter.Factory create(ObjectMapper objectMapper) {
        ObjectMapper mapper = Objects.requireNonNull(objectMapper, "mapper == null");
        return new BotAwareJacksonConverter(() -> mapper);
    }

    static Converter.Factory create(Supplier<ObjectMapper> objectMapperSupplier) {
        return new BotAwareJacksonConverter(objectMapperSupplier);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (type == BlobContent.class) {
            return BlobContent::new;
        }
        ObjectMapper objectMapper = objectMapperSupplier.get();
        JavaType javaType = objectMapper.constructType(type);
        var reader = objectMapper.readerFor(javaType);
        return body -> {
            try (body) {
                return reader.readValue(body.byteStream());
            } catch (JacksonIOException e) {
                throw e.getCause();
            } catch (JacksonException e) {
                throw new IOException(e.getMessage(), e);
            }
        };
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        if (type == UploadFile.class) {
            return this::buildRequestBodyFromUploadFile;
        }
        ObjectMapper objectMapper = objectMapperSupplier.get();
        JavaType javaType = objectMapper.constructType(type);
        var writer = objectMapper.writerFor(javaType);
        return value -> {
            try {
                return RequestBody.create(writer.writeValueAsBytes(value), JSON);
            } catch (JacksonIOException e) {
                throw e.getCause();
            } catch (JacksonException e) {
                throw new IOException(e.getMessage(), e);
            }
        };
    }

    private RequestBody buildRequestBodyFromUploadFile(Object o) {
        if (o instanceof UploadFile.StringUploadFile uploadFile) {
            return RequestBody.create(uploadFile.src(), MediaType.parse(uploadFile.contentType()));
        } else if (o instanceof UploadFile.FileUploadFile uploadFile) {
            return RequestBody.create(uploadFile.src(), MediaType.parse(uploadFile.contentType()));
        } else if (o instanceof UploadFile.ByteArrayUploadFile uploadFile) {
            return RequestBody.create(uploadFile.src(), MediaType.parse(uploadFile.contentType()));
        }

        throw new IllegalArgumentException("Unsupported object: " + o);
    }
}
