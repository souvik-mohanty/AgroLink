package com.agrolink.admin.repository;

import com.agrolink.admin.model.Complaint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends MongoRepository<Complaint, String> {
    List<Complaint> findByResolved(boolean resolved);
    List<Complaint> findByRaisedBy(String raisedBy);
}
