package com.abx.hollywoodtherapist.repository;

import com.abx.hollywoodtherapist.model.Summary;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SummaryRepository extends MongoRepository<Summary, String> {
    List<Summary> findByUserId(String userId);

    void deleteByIdIn(List<UUID> ids);
}
