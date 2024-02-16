package com.abx.hollywoodtherapist.service;

public interface GenerativeAIService<GenerativeAiPrompt, GenerativeAiResponse> {
    GenerativeAiResponse complete(GenerativeAiPrompt prompt);

    String parseGptResponse(GenerativeAiResponse response);
}