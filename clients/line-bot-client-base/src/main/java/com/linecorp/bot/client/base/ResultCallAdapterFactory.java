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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import kotlin.NotImplementedError;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// based on CompletableFutureCallAdapterFactory
public class ResultCallAdapterFactory extends CallAdapter.Factory {
    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != CompletableFuture.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException(
                    "CompletableFuture return type must be parameterized"
                            + " as CompletableFuture<Foo> or CompletableFuture<? extends Foo>");
        }
        Type innerType = getParameterUpperBound(0, (ParameterizedType) returnType);

        if (getRawType(innerType) != Result.class) {
            // Generic type is not Response<T>. Use it for body-only adapter.
            throw new NotImplementedError("Should not reach here");
        }

        // Generic type is Response<T>. Extract T and create the Response version of the adapter.
        if (!(innerType instanceof ParameterizedType)) {
            throw new IllegalStateException(
                    "Response must be parameterized" + " as Result<Foo> or Result<? extends Foo>");
        }
        Type responseType = getParameterUpperBound(0, (ParameterizedType) innerType);
        return new ResultCallAdapter<>(responseType);
    }

    private static final class ResultCallAdapter<R>
            implements CallAdapter<R, CompletableFuture<Result<R>>> {
        private final Type responseType;

        ResultCallAdapter(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public CompletableFuture<Result<R>> adapt(final Call<R> call) {
            CompletableFuture<Result<R>> future = new CallCancelCompletableFuture<>(call);
            call.enqueue(new ResultCallback(future));
            return future;
        }

        private class ResultCallback implements Callback<R> {
            private final CompletableFuture<Result<R>> future;

            ResultCallback(CompletableFuture<Result<R>> future) {
                this.future = future;
            }

            @Override
            public void onResponse(Call<R> call, Response<R> response) {
                // https://developers.line.biz/en/reference/messaging-api/#response-headers
                future.complete(new Result<>(
                        response.headers().get("X-Line-Request-Id"),
                        response.headers().get("X-Line-Accepted-Request-Id"),
                        response.body()));
            }

            @Override
            public void onFailure(Call<R> call, Throwable t) {
                future.completeExceptionally(t);
            }
        }
    }

    private static final class CallCancelCompletableFuture<T> extends CompletableFuture<T> {
        private final Call<?> call;

        CallCancelCompletableFuture(Call<?> call) {
            this.call = call;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            if (mayInterruptIfRunning) {
                call.cancel();
            }
            return super.cancel(mayInterruptIfRunning);
        }
    }
}
