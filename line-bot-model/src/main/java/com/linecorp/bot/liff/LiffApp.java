package com.linecorp.bot.liff;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class LiffApp {
    /** LIFF app ID */
    String liffId;

    /** {@link LiffView} object which contains the URL and view size of the LIFF app. */
    LiffView view;
}
