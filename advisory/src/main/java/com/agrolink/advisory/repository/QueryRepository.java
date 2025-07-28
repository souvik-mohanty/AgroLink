package com.agrolink.advisory.repository;

import com.agrolink.advisory.model.FarmerQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QueryRepository extends MongoRepository<FarmerQuery, String> {
}
