package com.abx.hollywoodtherapist.controller;

import com.abx.hollywoodtherapist.dto.ImmutableSessionDto;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatApiController {
    // TODO: Message mapping

    public String message(long userId, long sessionId) {
        ImmutableSessionDto.builder().sessionId(sessionId).systemPrompt("Hello, World!").build();
        return "Hello, World!";
    }
}
