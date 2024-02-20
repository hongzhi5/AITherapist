package com.abx.hollywoodtherapist.repository;

import com.abx.hollywoodtherapist.model.Summary;
import java.util.*;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SummaryRepository extends MongoRepository<Summary, String> {
    List<Summary> findBySessionId(String userId);

    void deleteByIdIn(List<UUID> ids);
}
