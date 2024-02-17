package com.abx.hollywoodtherapist.dto;

import org.immutables.value.Value;

@Value.Immutable
public interface SessionDto {

    String getSessionId();
    String getSystemPrompt();
    void setSessionId(String sessionId);
    void setSystemPrompt(String systemPrompt);

}
