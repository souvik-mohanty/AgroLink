package com.agrolink.advisory.repository;

import com.agrolink.advisory.model.AdvisoryContent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdvisoryRepository extends MongoRepository<AdvisoryContent, String> {
}
