package com.abx.hollywoodtherapist.service;

public interface GenerativeAiService<GenerativeAiPrompt, GenerativeAiResponse> {
    GenerativeAiResponse complete(GenerativeAiPrompt prompt);

    String parseGptResponse(GenerativeAiResponse response);
}
