/*
 * Copyright 2016 LINE Corporation
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

package com.linecorp.bot.client;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.NonNull;
import lombok.ToString;

/*
 * This class validates value of the `X-LINE-Signature` header.
 */
@ToString
public class LineSignatureValidator {
    private static final String HASH_ALGORITHM = "HmacSHA256";
    private final SecretKeySpec secretKeySpec;

    /**
     * Create new instance with channel secret.
     */
    public LineSignatureValidator(byte[] channelSecret) {
        this.secretKeySpec = new SecretKeySpec(channelSecret, HASH_ALGORITHM);
    }

    /**
     * Validate signature.
     *
     * @param content Body of the http request in byte array.
     * @param headerSignature Signature value from `X-LINE-Signature` HTTP header
     * @return True if headerSignature matches signature of the content. False otherwise.
     */
    public boolean validateSignature(@NonNull byte[] content, @NonNull String headerSignature) {
        final byte[] signature = generateSignature(content);
        final byte[] decodeHeaderSignature = Base64.getDecoder().decode(headerSignature);
        return MessageDigest.isEqual(decodeHeaderSignature, signature);
    }

    /**
     * Generate signature value.
     *
     * @param content Body of the http request.
     * @return generated signature value.
     */
    public byte[] generateSignature(@NonNull byte[] content) {
        try {
            Mac mac = Mac.getInstance(HASH_ALGORITHM);
            mac.init(secretKeySpec);
            return mac.doFinal(content);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            // "HmacSHA256" is always supported in Java 8 platform.
            //   (see https://docs.oracle.com/javase/8/docs/api/javax/crypto/Mac.html)
            // All valid-SecretKeySpec-instance are not InvalidKey.
            //   (because the key for HmacSHA256 can be of any length. see RFC2104)
            throw new IllegalStateException(e);
        }
    }

}

