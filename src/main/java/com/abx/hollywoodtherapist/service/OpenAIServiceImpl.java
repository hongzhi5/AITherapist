package com.abx.hollywoodtherapist.service;

import com.abx.hollywoodtherapist.dto.EmbeddingRequestDto;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OpenAIServiceImpl implements GenerativeAIService<String, List<CompletionChoice>> {
    @Value("${openai.api.model}")
    private String openAIModel;

    @Value("${openai.api.url}")
    private String openAIEndpoint;

    private final OpenAiService openAiService;
    public OpenAIServiceImpl(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public List<CompletionChoice> complete(String prompt) {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model(openAIModel)
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

    public List<ChatMessage> continueConversation(List<ChatMessage> conversation) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(conversation)
                .maxTokens(100)
                .build();

        ChatMessage responseMessage = openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
        conversation.add(responseMessage);

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
            allEmbeddings[i] = embeddingList.stream().mapToDouble(Double::doubleValue).toArray();
        }

        return allEmbeddings;
    }

}