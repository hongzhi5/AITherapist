package com.abx.hollywoodtherapist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TherapyService {

    @Autowired
    private  MessageService messageService;

    public String therapy(long userId, long sessionId, String systemPrompt) {
        return messageService.message(userId, sessionId);
    }
}
