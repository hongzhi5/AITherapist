package com.abx.hollywoodtherapist.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.abx.hollywoodtherapist.model.Conversation;
import com.abx.hollywoodtherapist.model.Summary;
import com.abx.hollywoodtherapist.repository.ConversationRepository;
import com.abx.hollywoodtherapist.repository.SummaryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TherapyServiceTest {

    MessageService messageService = Mockito.mock(MessageService.class);
    ConversationRepository conversationRepository = Mockito.mock(ConversationRepository.class);
    SummaryRepository summaryRepository = Mockito.mock(SummaryRepository.class);

    TherapyService therapyService = new TherapyService(messageService, conversationRepository, summaryRepository);

    Conversation mockConversation = Mockito.mock(Conversation.class);
    Summary mockSummary = Mockito.mock(Summary.class);

    @Test
    public void testSaveConversation() {
        when(conversationRepository.save(any(Conversation.class))).thenReturn(mockConversation);
        therapyService.saveConversation("userId", "userMessage", "user");
        verify(conversationRepository, times(1)).save(any(Conversation.class));
    }

    @Test
    public void testSaveSummary() {
        when(summaryRepository.save(any(Summary.class))).thenReturn(mockSummary);
        therapyService.saveSummary("userId", "summary");
        verify(summaryRepository, times(1)).save(any(Summary.class));
    }

    @Test
    public void testGetResponse_doNotSummarize() {
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId2", "userMessage", "systemPrompt");

        verify(summaryRepository, times(0)).save(any());
    }

    @Test
    public void testGetResponse_doSummarizeSummary() {
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        therapyService.getResponse("userId1", "userMessage", "systemPrompt");

        verify(summaryRepository, times(1)).save(any());
    }

    @Test
    public void testGetResponse_doSummerizeConversation() {
        for (int i = 0; i < 5; i++) {
            therapyService.getResponse("userId1", "userMessage", "systemPrompt");
        }
        verify(summaryRepository, times(1)).save(any());
    }
}
