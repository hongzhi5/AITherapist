package com.abx.hollywoodtherapist.repository;

import java.util.*;
import com.abx.hollywoodtherapist.model.Summary;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface SummaryRepository extends MongoRepository<Summary, String>{
    List<Summary> findBySessionId(String userId);
    void deleteByIdIn(List<UUID> ids);
}
