package com.abx.hollywoodtherapist.service;

import com.abx.hollywoodtherapist.model.Conversation;
import com.abx.hollywoodtherapist.model.Summary;
import com.abx.hollywoodtherapist.repository.ConversationRepository;
import com.abx.hollywoodtherapist.repository.SummaryRepository;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TherapyService {

    private final MessageService messageService;
    private final ConversationRepository conversationRepository;
    private final SummaryRepository summaryRepository;

    private final Map<String, Integer> convCountMap = new HashMap<>();
    private final Map<String, Integer> summaryCountMap = new HashMap<>();

    private SessionManager sessionManager;

    public TherapyService(
            MessageService messageService,
            ConversationRepository conversationRepository,
            SummaryRepository summaryRepository) {
        this.messageService = messageService;
        this.conversationRepository = conversationRepository;
        this.summaryRepository = summaryRepository;
    }

    public String getResponse(String userId, String userMessage, String systemPrompt) {
        String context = "";
        int convCount = convCountMap.getOrDefault(userId, 0);
        int summaryCount = summaryCountMap.getOrDefault(userId, 0);

        if (summaryCount == 0) {
            context = "";
        }
        String response = "Input context should be last n messages (when n < 5) or a summary of last 5 messages";
        saveConversation(userId, userMessage, "user");
        saveConversation(userId, response, "system");
        convCount++;
        convCountMap.put(userId, convCount);

        if (convCount % 5 != 0) {
            return response;
        }

        convCount = 0;
        convCountMap.put(userId, convCount);
        String summary = "Use ChatAPIService to get a summary of 5 messages";
        saveSummary(userId, summary);
        summaryCount++;
        summaryCountMap.put(userId, summaryCount);

        if (summaryCount % 5 != 0) {
            return response;
        }

        summaryCount = 1;
        summaryCountMap.put(userId, summaryCount);
        List<Summary> summaries = summaryRepository.findByUserId(userId);
        List<Summary> latestSummaries = summaries.stream()
                .sorted(Comparator.comparing(Summary::getTimeStamp).reversed())
                .limit(5)
                .toList();
        String mergedSummary = latestSummaries.stream().map(Summary::getContent).collect(Collectors.joining("\n"));
        List<UUID> idsToDelete = latestSummaries.stream().map(Summary::getId).toList();
        summaryRepository.deleteByIdIn(idsToDelete);
        // Send mergedSummary to ChatAPIService
        summary = "Use ChatAPIService to get a summary of 5 summaries";
        saveSummary(userId, summary);

        // TODO: Set a threshold to trigger deletion of old conversations
        return response;
    }

    public List<String> generateMovieTitles(String userId) {
        // Generate recommendation
        String systemPrompt =
                "You are a psychotherapist who has been following the conversation summaries with a client. "
                        + "Based on the insights gained from these summaries "
                        + "about the client's emotional state, interests, "
                        + "and the issues they are facing, please recommend "
                        + "a series of movies that could resonate with, inspire, "
                        + "or provide solace to the client. List each movie "
                        + "title separated by a \"#\" symbol, and ensure that only "
                        + "the titles are mentioned without any additional content or commentary.\n";
        String summary = getMergedSummaries(userId);
        // TODO: Use ChatAPIService message(systemPrompt + summary)
        List<String> movieTitles = Arrays.asList(summary.split("#"));
        return movieTitles;
    }

    public void generateTitlesEmbedding(String userId, String content) {
        String systemPrompt = "Please provide a summary of the last 5 messages";
        for (String title : generateMovieTitles(userId)) {
            // TODO: Use ChatAPIService to generate embedding for each title
        }
    }

    public void getMovieList(String userId) {}

    public String getMergedSummaries(String userId) {
        List<Summary> summaries = summaryRepository.findByUserId(userId);
        List<Summary> latestSummaries = summaries.stream()
                .sorted(Comparator.comparing(Summary::getTimeStamp).reversed())
                .limit(5)
                .toList();
        return latestSummaries.stream().map(Summary::getContent).collect(Collectors.joining("\n"));
    }

    public void saveConversation(String userId, String content, String type) {
        // Save conversation to database
        Conversation conversation = new Conversation.Builder()
                .setUserId(userId)
                .setContent(content)
                .setTimeStamp(System.currentTimeMillis())
                .setType(type)
                .build();
        conversationRepository.save(conversation);
    }

    public void saveSummary(String userId, String content) {
        // Save summary to database
        Summary summary = new Summary.Builder()
                .setUserId(userId)
                .setSessionId(userId)
                .setTimeStamp(System.currentTimeMillis())
                .setContent(content)
                .build();
        summaryRepository.save(summary);
    }
}
