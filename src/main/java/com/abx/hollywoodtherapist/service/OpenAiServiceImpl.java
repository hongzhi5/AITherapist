package com.abx.hollywoodtherapist.service;

import com.abx.hollywoodtherapist.dto.EmbeddingRequestDto;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OpenAiServiceImpl implements GenerativeAiService<String, List<CompletionChoice>> {
    @Value("${openai.api.embedding.model}")
    private String openAiEmbeddingModel;

    @Value("${openai.api.chat.model}")
    private String openAiChatModel;

    private final OpenAiService openAiService;

    private static final Logger log = LoggerFactory.getLogger(OpenAiServiceImpl.class);

    public OpenAiServiceImpl(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public List<CompletionChoice> complete(String prompt) {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model(openAiChatModel)
                .echo(true)
                .build();
        return openAiService.createCompletion(completionRequest).getChoices();
    }

    @Override
    public String parseGptResponse(List<CompletionChoice> completionChoices) {
        StringBuilder ans = new StringBuilder();
        for (CompletionChoice choice : completionChoices) {
            ans.append(parseSingleGptResponse(choice));
        }
        return ans.toString();
    }

    private String parseSingleGptResponse(CompletionChoice choice) {
        return choice.getText();
    }

    public List<ChatMessage> continueConversation(List<ChatMessage> originalConversation, String userPrompt) {
        List<ChatMessage> conversation =
                new ArrayList<>(originalConversation != null ? originalConversation : Collections.emptyList());
        if (conversation.isEmpty()) {
            conversation.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are talking to an AI therapist."));
        }

        if (userPrompt != null && !userPrompt.isEmpty()) {
            conversation.add(new ChatMessage(ChatMessageRole.USER.value(), userPrompt));
        }

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(openAiChatModel)
                .messages(conversation)
                .maxTokens(100)
                .build();
        try {
            ChatMessage responseMessage = openAiService
                    .createChatCompletion(chatCompletionRequest)
                    .getChoices()
                    .get(0)
                    .getMessage();
            conversation.add(responseMessage);
        } catch (Exception e) {
            log.error("Error during chat completion: {}", e.getMessage(), e);
            conversation.add(new ChatMessage(
                    ChatMessageRole.SYSTEM.value(), "Sorry, there was a problem processing your request."));
        }

        return conversation;
    }

    public double[][] getEmbeddings(EmbeddingRequestDto requestDto) {
        EmbeddingRequest openAiRequest = EmbeddingRequest.builder()
                .model(requestDto.getModel())
                .input(requestDto.getInput())
                .build();

        EmbeddingResult result = openAiService.createEmbeddings(openAiRequest);
        double[][] allEmbeddings = new double[result.getData().size()][];

        for (int i = 0; i < result.getData().size(); i++) {
            List<Double> embeddingList = result.getData().get(i).getEmbedding();
            allEmbeddings[i] =
                    embeddingList.stream().mapToDouble(Double::doubleValue).toArray();
        }

        return allEmbeddings;
    }

    public String summarizeText(String inputText) {
        String prompt = "Summarize the following text:\n\n" + inputText;

        CompletionRequest request = CompletionRequest.builder()
                .model(openAiChatModel)
                .prompt(prompt)
                .maxTokens(200)
                .temperature(0.5)
                .topP(1.0)
                .build();

        try {
            CompletionResult response = openAiService.createCompletion(request);
            if (!response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getText().trim();
            }
        } catch (Exception e) {
            log.error("Error summarizing text: {}", e.getMessage(), e);
            return "Error summarizing text.";
        }

        return "Unable to generate summary.";
    }
}
