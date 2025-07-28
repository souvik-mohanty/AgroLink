package com.agrolink.admin.repository;

import com.agrolink.admin.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {
    List<Report> findByType(String type);
    List<Report> findByGeneratedOnBetween(Date startDate, Date endDate);
}
