package com.abx.hollywoodtherapist.service;

import com.abx.hollywoodtherapist.dto.ImmutableSessionDto;
import com.abx.hollywoodtherapist.dto.SessionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TherapyService {

    private final MessageService messageService;
    private final Map<String, List<SessionDto>> sessionMap = new HashMap<>();

    public TherapyService(MessageService messageService) {
        this.messageService = messageService;
    }

    public SessionDto startSession(String userId) {
        SessionDto sessionDto = ImmutableSessionDto.builder()
                .sessionId(generateSessionId())
                .systemPrompt("Hello, how can I help you today?")
                .build();

        // If userId exist, add sessionDto to list. If not, create new list and add sessionDto to it.
        if (sessionMap.containsKey(userId)) {
            sessionMap.get(userId).add(sessionDto);
        } else {
            sessionMap.put(userId, List.of(sessionDto));
        }

        return sessionDto;
    }

    public String therapy(String userId, String sessionId, String systemPrompt) {
        return messageService.message(userId, sessionId);
    }

    public void endSession(String userId, String sessionId) {
        sessionMap.get(userId).removeIf(sessionDto -> sessionDto.getSessionId().equals(sessionId));
    }
}
