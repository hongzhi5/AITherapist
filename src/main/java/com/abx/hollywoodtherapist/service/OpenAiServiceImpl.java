package com.abx.hollywoodtherapist.service;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.service.OpenAiService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public String continueConversation(List<ChatMessage> userPrompts, List<ChatMessage> gptResponses) {
        List<ChatMessage> fullConversation = new ArrayList<>();
        fullConversation.addAll(userPrompts);
        fullConversation.addAll(gptResponses);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(openAiChatModel)
                .messages(fullConversation)
                .maxTokens(200)
                .build();

        try {
            ChatMessage newGptResponse = openAiService
                    .createChatCompletion(chatCompletionRequest)
                    .getChoices()
                    .get(0)
                    .getMessage();

            return newGptResponse.getContent();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
            return "Something went wrong";
        }
    }

    public List<Double> getEmbeddings(String input) {
        String defaultModel = openAiEmbeddingModel;
        List<String> inputs = Collections.singletonList(input);

        EmbeddingRequest openAiRequest =
                EmbeddingRequest.builder().model(defaultModel).input(inputs).build();

        EmbeddingResult result = openAiService.createEmbeddings(openAiRequest);
        if (!result.getData().isEmpty()
                && !result.getData().get(0).getEmbedding().isEmpty()) {
            return result.getData().get(0).getEmbedding();
        } else {
            return Collections.emptyList();
        }
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
