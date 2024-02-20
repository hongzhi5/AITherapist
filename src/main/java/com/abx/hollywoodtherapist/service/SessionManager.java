package com.abx.hollywoodtherapist.service;

import com.abx.hollywoodtherapist.dto.ImmutableSessionDto;
import com.abx.hollywoodtherapist.dto.SessionDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private final Map<String, List<SessionDto>> sessionMap = new HashMap<>();

    public String startSession(String userId) {
        String sessionId = generateSessionId();
        SessionDto sessionDto = ImmutableSessionDto.builder()
                .sessionId(sessionId)
                .systemPrompt("Hello, how can I help you today?")
                .build();
        return sessionId;
    }

    public void removeSession(String userId, String sessionId) {
        sessionMap.get(userId).removeIf(sessionDto -> sessionDto.getSessionId().equals(sessionId));
    }

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
