package com.abx.hollywoodtherapist.model;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "conversation")
public class Conversation {
    @Id
    private UUID id;
    private String userId;
    private String sessionId;
    private LocalDateTime timeStamp;
    private String content;

    private String type;

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

    public String getType() {
        return type;
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

    public void setType(String type) {
        this.type = type;
    }

    // Constructors
    public Conversation() {

    }

}
