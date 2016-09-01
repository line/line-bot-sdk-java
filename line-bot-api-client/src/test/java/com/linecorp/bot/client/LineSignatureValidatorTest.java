package com.linecorp.bot.client;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class LineSignatureValidatorTest {
    private static final String channelSecret = "SECRET";

    @Test
    public void validateSignature() throws Exception {
        LineSignatureValidator lineSignatureValidator = new LineSignatureValidator(
                channelSecret.getBytes(StandardCharsets.UTF_8));

        String httpRequestBody = "{}";
        assertTrue(lineSignatureValidator
                           .validateSignature(httpRequestBody.getBytes(StandardCharsets.UTF_8),
                                              "3q8QXTAGaey18yL8FWTqdVlbMr6hcuNvM4tefa0o9nA="
                           ));
        assertFalse(lineSignatureValidator
                            .validateSignature(httpRequestBody.getBytes(StandardCharsets.UTF_8),
                                               "596359635963"
                            ));
    }

    @Test
    public void generateSignature() throws Exception {
        LineSignatureValidator lineSignatureValidator = new LineSignatureValidator(
                channelSecret.getBytes(StandardCharsets.UTF_8));

        String httpRequestBody = "{}";
        byte[] headerSignature = lineSignatureValidator
                .generateSignature(httpRequestBody.getBytes(StandardCharsets.UTF_8));

        assertEquals("3q8QXTAGaey18yL8FWTqdVlbMr6hcuNvM4tefa0o9nA=",
                     Base64.encodeBase64String(headerSignature));
    }

}
