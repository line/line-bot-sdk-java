package com.linecorp.bot.client.exception;

import com.linecorp.bot.model.error.ErrorResponse;

public class NotFoundException extends LineMessagingException {
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public NotFoundException(
            final String message,
            final ErrorResponse errorResponse) {
        super(message, errorResponse, null);
    }
}
