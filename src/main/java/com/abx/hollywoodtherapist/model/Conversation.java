package com.abx.hollywoodtherapist.model;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "conversation")
public class Conversation {
    private Conversation() {}
    @Id
    private UUID id;
    private String userId;
    private String sessionId;
    private long timeStamp;
    private String content;
    private String type;

    public static class Builder {
        private Conversation conversation;

        public Builder() {
            conversation = new Conversation();
        }
        public Builder setId(UUID id) {
            conversation.id = id;
            return this;
        }

        public Builder setUserId(String userId) {
            conversation.userId = userId;
            return this;
        }

        public Builder setSessionId(String sessionId) {
            conversation.sessionId = sessionId;
            return this;
        }

        public Builder setTimeStamp(long timeStamp) {
            conversation.timeStamp = timeStamp;
            return this;
        }

        public Builder setContent(String content) {
            conversation.content = content;
            return this;
        }

        public Builder setType(String type) {
            conversation.type = type;
            return this;
        }

        public Conversation build() {
            return conversation;
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

    public String getType() {
        return type;
    }
}
