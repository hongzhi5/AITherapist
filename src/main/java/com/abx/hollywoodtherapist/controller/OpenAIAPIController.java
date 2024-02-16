package com.abx.hollywoodtherapist.controller;

import com.abx.hollywoodtherapist.service.OpenAIServiceImpl;
import com.abx.hollywoodtherapist.dto.*;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OpenAIAPIController {

    private final OpenAIServiceImpl openAiService;

    public OpenAIAPIController(OpenAIServiceImpl openAiService) {
        this.openAiService = openAiService;
    }

    @ModelAttribute("conversation")
    public List<String> conversation() {
        return new ArrayList<>();
    }

    @GetMapping("/chat")
    public List<ChatMessage> chat(@RequestParam String prompt, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<ChatMessage> messages = (List<ChatMessage>) session.getAttribute("conversation");
        if (messages == null) {
            messages = new ArrayList<>();
            messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are talking to an AI therapist."));
        }

        messages.add(new ChatMessage(ChatMessageRole.USER.value(), prompt));
        List<ChatMessage> updatedMessages = openAiService.continueConversation(messages);
        session.setAttribute("conversation", updatedMessages);
        return updatedMessages;
    }
    @PostMapping("/chat/reset")
    public String resetConversation(WebRequest request) {
        request.removeAttribute("conversation", WebRequest.SCOPE_SESSION);
        return "Conversation reset.";
    }

    @PostMapping("/embeddings")
    public double[][] getEmbeddings(@RequestBody EmbeddingRequestDto requestDto) {
        return openAiService.getEmbeddings(requestDto);
    }
}
