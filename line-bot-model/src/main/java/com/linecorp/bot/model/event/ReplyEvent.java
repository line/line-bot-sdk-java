package com.linecorp.bot.model.event;

import com.linecorp.bot.model.ReplyMessage;

/**
 * Interface for reply support.
 *
 * @see Event
 */
public interface ReplyEvent {
    /**
     * Token for replying to this event.
     *
     * @see ReplyMessage
     * @see <a href="https://devdocs.line.me/#reply-message">//devdocs.line.me/#reply-message</a> &gt; Request Body
     */
    String getReplyToken();
}
