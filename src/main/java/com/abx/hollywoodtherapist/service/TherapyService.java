package com.abx.hollywoodtherapist.service;

import com.abx.hollywoodtherapist.dto.ImmutableSessionDto;
import com.abx.hollywoodtherapist.dto.SessionDto;
import com.abx.hollywoodtherapist.model.*;
import com.abx.hollywoodtherapist.repository.*;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TherapyService {

    private final MessageService messageService;
    private final ConversationRepository conversationRepository;
    private final SummaryRepository summaryRepository;

    private int convCount = 0;
    private int summaryCount = 0;
    private SessionManager sessionManager;

    public TherapyService(MessageService messageService, ConversationRepository conversationRepository, SummaryRepository summaryRepository) {
        this.messageService = messageService;
        this.conversationRepository = conversationRepository;
        this.summaryRepository = summaryRepository;
    }

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

    public String getResponse(String userId, String userMessage, String systemPrompt) {
        String context = "";
        if (summaryCount == 0) {
            context = "";
        }
        String response = "Input context should be last n messages (when n < 5) or a summary of last 5 messages";
        saveConversation(userId, userMessage, "user");
        saveConversation(userId, response, "system");
        convCount ++;
        if (convCount % 5 == 0) {
            convCount = 0;
            String summary = "Use ChatAPIService to get a summary of 5 messages";
            saveSummary(userId, summary);
            summaryCount ++;
            if (summaryCount % 5 == 0) {
                summaryCount = 1;
                List<Summary> summaries = summaryRepository.findBySessionId(userId);
                List<Summary> lastestSummaries = summaries.stream()
                        .sorted(Comparator.comparing(Summary::getTimeStamp).reversed())
                        .limit(5)
                        .toList();
                String mergedSummary = lastestSummaries.stream()
                        .map(Summary::getContent)
                        .collect(Collectors.joining("\n"));
                List<UUID> idsToDelete = lastestSummaries.stream()
                        .map(Summary::getId)
                        .toList();
                summaryRepository.deleteByIdIn(idsToDelete);
                // Send mergedSummary to ChatAPIService
                summary = "Use ChatAPIService to get a summary of 5 summaries";
                saveSummary(userId, summary);
            }
        }
        // TODO: Set a threshold to trigger deletion of old conversations
        return response;
    }

    public void saveConversation(String userId, String content, String type) {
        // Save conversation to database
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setContent(content);
        conversation.setType(type);
        conversation.setTimeStamp(java.time.LocalDateTime.now());
        conversationRepository.save(conversation);
    }

    public void saveSummary(String userId, String content) {
        // Save summary to database
        Summary summary = new Summary();
        summary.setUserId(userId);
        summary.setContent(content);
        summary.setTimeStamp(java.time.LocalDateTime.now());
        summaryRepository.save(summary);
    }
}
