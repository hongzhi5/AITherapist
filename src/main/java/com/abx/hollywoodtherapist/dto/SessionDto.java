package com.abx.hollywoodtherapist.dto;

import org.immutables.value.Value;

@Value.Immutable
public interface SessionDto {

    String getSessionId();

    String getSystemPrompt();
}
