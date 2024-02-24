package com.abx.hollywoodtherapist.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.abx.hollywoodtherapist.model.Conversation;
import com.abx.hollywoodtherapist.model.Summary;
import com.abx.hollywoodtherapist.repository.ConversationRepository;
import com.abx.hollywoodtherapist.repository.SummaryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class TherapyServiceTest {

    private final MessageService messageService = Mockito.mock(MessageService.class);
    private final ConversationRepository conversationRepository = Mockito.mock(ConversationRepository.class);
    private final SummaryRepository summaryRepository = Mockito.mock(SummaryRepository.class);

    private final TherapyService therapyService =
            new TherapyService(messageService, conversationRepository, summaryRepository);

    private final Conversation mockConversation = Mockito.mock(Conversation.class);
    private final Summary mockSummary = Mockito.mock(Summary.class);

    @Test
    public void testSaveConversation() {
        when(conversationRepository.save(ArgumentMatchers.any(Conversation.class)))
                .thenReturn(mockConversation);
        therapyService.saveConversation("userId", "userMessage", "user");
        verify(conversationRepository, times(1)).save(ArgumentMatchers.any(Conversation.class));
    }

    @Test
    public void testSaveSummary() {
        when(summaryRepository.save(ArgumentMatchers.any(Summary.class))).thenReturn(mockSummary);
        therapyService.saveSummary("userId", "summary");
        verify(summaryRepository, times(1)).save(ArgumentMatchers.any(Summary.class));
    }

    @Test
    public void testGetResponse_doNotSummarize() {
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId2", "userMessage", "systemPrompt");

        verify(summaryRepository, times(0)).save(ArgumentMatchers.any());
    }

    @Test
    public void testGetResponse_doSummarizeSummary() {
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");

        verify(summaryRepository, times(1)).save(ArgumentMatchers.any());
    }

    @Test
    public void testGetResponse_doSummarizeConversation() {
        for (int i = 0; i < 5; i++) {
            therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        }
        verify(summaryRepository, times(1)).save(ArgumentMatchers.any());
    }
}
