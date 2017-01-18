package com.linecorp.bot.model;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.linecorp.bot.model.message.Message;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Send messages to multiple users, groups, and rooms at any time.
 */
@Value
@AllArgsConstructor
public class Multicast {
    /**
     * IDs of the receivers.<br />
     * Max: 150
     *
     * <p>INFO: Use IDs returned via the webhook event of source users. IDs of groups or rooms cannot be used.</p>
     */
    private final Set<String> to;

    /**
     * List of Message objects.<br />
     * Max: 5
     */
    private final List<Message> messages;

    public Multicast(final Set<String> to, final Message message) {
        this.to = to;
        this.messages = Collections.singletonList(message);
    }
}
