package com.abx.hollywoodtherapist;

import com.abx.hollywoodtherapist.service.OpenAiServiceImpl;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class OpenAiServiceImplTest {
    private final OpenAiService openAiService = Mockito.mock(OpenAiService.class);

    private final OpenAiServiceImpl openAiServiceImpl = new OpenAiServiceImpl(openAiService);

    private final ChatCompletionResult mockResult = Mockito.mock(ChatCompletionResult.class);

    @Test
    public void testContinueConversation() {
        ChatCompletionChoice mockChoice = Mockito.mock(ChatCompletionChoice.class);
        ChatMessage mockResponseMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "Mocked AI Response");

        Mockito.when(mockChoice.getMessage()).thenReturn(mockResponseMessage);
        Mockito.when(mockResult.getChoices()).thenReturn(List.of(mockChoice));
        Mockito.when(openAiService.createChatCompletion(ArgumentMatchers.any(ChatCompletionRequest.class)))
                .thenReturn(mockResult);

        List<ChatMessage> originalConversation = List.of();
        String userPrompt = "Hello, how are you?";
        List<ChatMessage> updatedConversation =
                openAiServiceImpl.continueConversation(originalConversation, userPrompt);

        Assertions.assertFalse(updatedConversation.isEmpty(), "The conversation should not be empty.");
        Assertions.assertEquals(3, updatedConversation.size(), "The conversation should contain three messages.");
        Assertions.assertEquals(
                ChatMessageRole.SYSTEM.value(),
                updatedConversation.get(0).getRole(),
                "The first message should be from the system.");
        Assertions.assertEquals(
                ChatMessageRole.USER.value(),
                updatedConversation.get(1).getRole(),
                "The second message should be from the user.");
        Assertions.assertEquals(userPrompt, updatedConversation.get(1).getContent(), "The user's prompt should match.");
        Assertions.assertEquals(
                ChatMessageRole.SYSTEM.value(),
                updatedConversation.get(2).getRole(),
                "The third message should be from the AI.");
        Assertions.assertEquals(
                "Mocked AI Response",
                updatedConversation.get(2).getContent(),
                "The AI's response should match the mocked response.");
    }
}
