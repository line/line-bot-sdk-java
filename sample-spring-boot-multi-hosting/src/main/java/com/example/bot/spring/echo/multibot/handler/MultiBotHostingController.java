package com.example.bot.spring.echo.multibot.handler;

import static com.example.bot.spring.echo.multibot.handler.CallbackContext.MDC_KEY_BOT_ID;
import static com.example.bot.spring.echo.multibot.handler.CallbackContext.MDC_KEY_EVENT_ID;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.io.IOException;

import javax.servlet.ServletRequest;

import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.bot.spring.echo.multibot.MultiBotEventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;
import com.linecorp.bot.parser.LineSignatureValidator;
import com.linecorp.bot.parser.WebhookParseException;
import com.linecorp.bot.parser.WebhookParser;
import com.linecorp.bot.spring.boot.LineBotProperties;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class MultiBotHostingController {

    private final ObjectMapper mapper = ModelObjectMapper.createNewObjectMapper();
    private final MultiBotProtperties multiBotProtperties;
    private final MultiBotEventHandler multiBotEventHandler;

    @PostMapping("/callback/{configurationKey}")
    public ResponseEntity<?> handleCallback(
            @RequestHeader(WebhookParser.SIGNATURE_HEADER_NAME) String signature,
            @PathVariable String configurationKey,
            ServletRequest servletRequest
    ) throws IOException {
        final byte[] requestAsBytes = StreamUtils.copyToByteArray(servletRequest.getInputStream());
        final CallbackRequest unverifiedCallbackRequest =
                mapper.readValue(requestAsBytes, CallbackRequest.class);
        final String botId = unverifiedCallbackRequest.getDestination();
        try {
            MDC.put(MDC_KEY_BOT_ID, botId);
            log.debug("CallbackRequest (not verified yet): {}", unverifiedCallbackRequest);

            final LineBotProperties lineBotProperties = multiBotProtperties
                    .getChannelToPropertiesMap()
                    .get(configurationKey);

            if (lineBotProperties == null) {
                log.info("There are no configuration for botId = {}", botId);
                return ResponseEntity.badRequest().build();
            }

            return handleCallbackInternal(signature, requestAsBytes, botId, lineBotProperties);
        } finally {
            MDC.remove(MDC_KEY_BOT_ID);
        }

    }

    private ResponseEntity<?> handleCallbackInternal(
            final String signature,
            final byte[] requestAsBytes,
            final String botId,
            final LineBotProperties lineBotProperties) throws IOException {
        final WebhookParser webhookParser = buildWebhookParser(lineBotProperties);

        final CallbackRequest callbackRequest;
        try {
            callbackRequest = webhookParser.handle(signature, requestAsBytes);
            log.debug("Validation passed");
        } catch (WebhookParseException e) {
            log.warn("Request with invalid signature. (Maybe configuration miss?)");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        int callbackRequstHashCode = callbackRequest.hashCode();

        boolean noError = true;

        for (Event event : callbackRequest.getEvents()) {
            try {
                final String eventId = String.format("%08X", event.hashCode() + callbackRequstHashCode);
                MDC.put(MDC_KEY_EVENT_ID, eventId);
                final CallbackContext ctx = CallbackContext
                        .builder()
                        .botId(botId)
                        .channelToken(lineBotProperties.getChannelToken())
                        .eventId(eventId)
                        .event(event)
                        .build();
                multiBotEventHandler.accept(ctx, event);
            } catch (Exception e) {
                log.error("Exception in handling event: {}. Continue next", event, e);
                noError = false;
            } finally {
                MDC.remove(MDC_KEY_EVENT_ID);
            }
        }

        return noError
               ? ResponseEntity.ok().build()
               : ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
    }

    private WebhookParser buildWebhookParser(final LineBotProperties lineBotProperties) {
        final LineSignatureValidator lineSignatureValidator =
                new LineSignatureValidator(lineBotProperties.getChannelSecret().getBytes());
        return new WebhookParser(lineSignatureValidator);
    }
}
