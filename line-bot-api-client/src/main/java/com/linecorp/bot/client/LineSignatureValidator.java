package com.linecorp.bot.client;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.NonNull;
import lombok.ToString;

@ToString
public class LineSignatureValidator {
    private static final String HASH_ALGORITHM = "HmacSHA256";
    private final Mac mac;

    public LineSignatureValidator(byte[] channelSecret) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(channelSecret, HASH_ALGORITHM);

        this.mac = Mac.getInstance(HASH_ALGORITHM);
        this.mac.init(secretKeySpec);
    }

    public boolean validateSignature(@NonNull byte[] content, @NonNull String headerSignature) {
        final byte[] signature = generateSignature(content);
        final byte[] decodeHeaderSignature = Base64.getDecoder().decode(headerSignature);
        return MessageDigest.isEqual(decodeHeaderSignature, signature);
    }

    public byte[] generateSignature(@NonNull byte[] content) {
        return mac.doFinal(content);
    }

}

