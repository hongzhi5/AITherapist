package com.abx.hollywoodtherapist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;

import com.abx.hollywoodtherapist.service.OpenAiServiceImpl;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class OpenAiServiceImplTest {
    OpenAiService openAiService = Mockito.mock(OpenAiService.class);

    OpenAiServiceImpl openAiServiceImpl = new OpenAiServiceImpl(openAiService);

    ChatCompletionResult mockResult = Mockito.mock(ChatCompletionResult.class);

    @Test
    public void testContinueConversation() {
        ChatCompletionChoice mockChoice = Mockito.mock(ChatCompletionChoice.class);
        ChatMessage mockResponseMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "Mocked AI Response");

        Mockito.when(mockChoice.getMessage()).thenReturn(mockResponseMessage);
        Mockito.when(mockResult.getChoices()).thenReturn(List.of(mockChoice));
        Mockito.when(openAiService.createChatCompletion(any(ChatCompletionRequest.class)))
                .thenReturn(mockResult);

        List<ChatMessage> originalConversation = List.of();
        String userPrompt = "Hello, how are you?";
        List<ChatMessage> updatedConversation =
                openAiServiceImpl.continueConversation(originalConversation, userPrompt);

        assertFalse(updatedConversation.isEmpty(), "The conversation should not be empty.");
        assertEquals(3, updatedConversation.size(), "The conversation should contain three messages.");
        assertEquals(
                ChatMessageRole.SYSTEM.value(),
                updatedConversation.get(0).getRole(),
                "The first message should be from the system.");
        assertEquals(
                ChatMessageRole.USER.value(),
                updatedConversation.get(1).getRole(),
                "The second message should be from the user.");
        assertEquals(userPrompt, updatedConversation.get(1).getContent(), "The user's prompt should match.");
        assertEquals(
                ChatMessageRole.SYSTEM.value(),
                updatedConversation.get(2).getRole(),
                "The third message should be from the AI.");
        assertEquals(
                "Mocked AI Response",
                updatedConversation.get(2).getContent(),
                "The AI's response should match the mocked response.");
    }
}
