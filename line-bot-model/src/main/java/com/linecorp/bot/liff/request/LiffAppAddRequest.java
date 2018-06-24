package com.linecorp.bot.liff.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.linecorp.bot.liff.LiffView;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class LiffAppAddRequest {
    /** {@link LiffView} object which contains the URL and view size of the LIFF app. */
    LiffView view;
}
