package com.abx.hollywoodtherapist.repository;

import com.abx.hollywoodtherapist.model.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface ConversationRepository  extends MongoRepository<Conversation, String>{

}
