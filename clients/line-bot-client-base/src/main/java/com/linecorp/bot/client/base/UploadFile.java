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

// Wrap okhttp classes to help future feature improvements.

import java.io.File;

public interface UploadFile {
    /**
     * Upload file from String.
     */
    static StringUploadFile fromString(String src, String contentType) {
        return new StringUploadFile(src, contentType);
    }

    /**
     * Upload file from String.
     * content-type is hard-coded as `text/plain`.
     *
     * @deprecated use `fromString(String, String)` instead.
     */
    @Deprecated
    static StringUploadFile fromString(String src) {
        return new StringUploadFile(src, "text/plain");
    }

    /**
     * Upload file from File.
     */
    static FileUploadFile fromFile(File src, String contentType) {
        return new FileUploadFile(src, contentType);
    }

    /**
     * Upload file from File.
     * content-type is hard-coded as `text/plain`.
     *
     * @deprecated use `fromFile(File, String)` instead.
     */
    @Deprecated
    static FileUploadFile fromFile(File src) {
        return new FileUploadFile(src, "text/plain");
    }

    record StringUploadFile(
            String src,
            String contentType
    ) implements UploadFile {
    }

    record FileUploadFile(
            File src,
            String contentType
    ) implements UploadFile {
    }
}
