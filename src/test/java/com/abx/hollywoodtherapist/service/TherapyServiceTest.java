package com.abx.hollywoodtherapist.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.abx.hollywoodtherapist.model.Conversation;
import com.abx.hollywoodtherapist.repository.ConversationRepository;
import com.abx.hollywoodtherapist.repository.SummaryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class TherapyServiceTest {

    @Mock
    MessageService messageService;
    ConversationRepository conversationRepository = Mockito.mock(ConversationRepository.class);
    SummaryRepository summaryRepository = Mockito.mock(SummaryRepository.class);

    TherapyService therapyService = new TherapyService(messageService, conversationRepository, summaryRepository);


    @Test
    public void testSaveConversation() {
        when(conversationRepository.save(any(Conversation.class))).thenReturn(new Conversation());
        therapyService.saveConversation("userId", "userMessage", "user");
        verify(conversationRepository, times(1)).save(any(Conversation.class));

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
}
