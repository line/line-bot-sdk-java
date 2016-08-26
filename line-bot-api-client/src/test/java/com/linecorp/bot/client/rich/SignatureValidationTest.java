package com.linecorp.bot.client.rich;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.LineBotClientBuilder;

public class SignatureValidationTest {

    private static final String channelSecret = "SECRET";

    private static final Charset charset = StandardCharsets.UTF_8;

    @Test
    public void validSignatureTest() throws Exception {
        LineBotClient client = LineBotClientBuilder.create("CID", channelSecret).build();

        String httpRequestBody = "{}";
        String headerSignature = computeSignature(httpRequestBody);

        assertThat(client.validateSignature(httpRequestBody, headerSignature), is(true));
        assertThat(client.validateSignature(httpRequestBody.getBytes(charset), headerSignature), is(true));
    }

    @Test
    public void invalidSignatureTest() throws Exception {
        LineBotClient client = LineBotClientBuilder.create("CID", channelSecret).build();

        String httpRequestBody = "{}";
        String headerSignature = computeSignature(httpRequestBody);

        String alteredRequestBody = "{altered}";

        assertThat(client.validateSignature(alteredRequestBody, headerSignature), is(false));
        assertThat(client.validateSignature(alteredRequestBody.getBytes(charset), headerSignature), is(false));
    }

    private static String computeSignature(String httpRequestBody) throws Exception {
        // signature generation code from developers.line.me
        SecretKeySpec key = new SecretKeySpec(channelSecret.getBytes(charset), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        byte[] source = httpRequestBody.getBytes(charset);
        return Base64.encodeBase64String(mac.doFinal(source));
    }
}
