package com.linecorp.bot.liff.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.linecorp.bot.liff.LiffApp;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class LiffAppsResponse {
    /** Array of LIFF app objects */
    List<LiffApp> apps;
}
