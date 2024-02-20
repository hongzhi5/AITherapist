package com.abx.hollywoodtherapist.model;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "summary")
public class Summary {
    @Id
    private UUID id;

    private String userId;
    private String sessionId;
    private LocalDateTime timeStamp;
    private String content;

    // Getters
    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getContent() {
        return content;
    }

    // Setters

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Constructors
    public Summary(UUID id, String userId, String sessionId, LocalDateTime timeStamp, String content) {
        this.id = id;
        this.userId = userId;
        this.sessionId = sessionId;
        this.timeStamp = timeStamp;
        this.content = content;
    }

    public Summary() {}
}
