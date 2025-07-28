package com.agrolink.admin.repository;

import com.agrolink.admin.model.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends MongoRepository<Listing, String> {
    List<Listing> findByApproved(boolean approved);
    List<Listing> findByUserId(String userId);
}
