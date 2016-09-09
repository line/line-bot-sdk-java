package com.linecorp.bot.model.event.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes({
                      @JsonSubTypes.Type(TextMessageContent.class),
                      @JsonSubTypes.Type(ImageMessageContent.class),
                      @JsonSubTypes.Type(LocationMessageContent.class),
                      @JsonSubTypes.Type(AudioMessageContent.class),
                      @JsonSubTypes.Type(VideoMessageContent.class),
                      @JsonSubTypes.Type(StickerMessageContent.class)
              })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
public interface MessageContent {
    String getId();
}
