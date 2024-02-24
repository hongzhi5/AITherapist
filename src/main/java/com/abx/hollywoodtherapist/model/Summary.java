package com.abx.hollywoodtherapist.model;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "summary")
public class Summary {
    private Summary() {}

    @Id
    private UUID id;

    private String userId;
    private String sessionId;
    private long timeStamp;
    private String content;

    public static class Builder {
        private Summary summary;

        public Builder() {
            summary = new Summary();
        }

        public Builder setId(UUID id) {
            summary.id = id;
            return this;
        }

        public Builder setUserId(String userId) {
            summary.userId = userId;
            return this;
        }

        public Builder setSessionId(String sessionId) {
            summary.sessionId = sessionId;
            return this;
        }

        public Builder setTimeStamp(long timeStamp) {
            summary.timeStamp = timeStamp;
            return this;
        }

        public Builder setContent(String content) {
            summary.content = content;
            return this;
        }

        public Summary build() {
            return summary;
        }
    }

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

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getContent() {
        return content;
    }
}
