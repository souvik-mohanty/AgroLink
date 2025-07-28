package com.agrolink.admin.repository;

import com.agrolink.admin.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findBySuspended(boolean suspended);
    List<User> findByRole(String role);
}
