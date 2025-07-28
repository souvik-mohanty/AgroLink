package com.agrolink.advisory.service;

import com.agrolink.advisory.dto.QueryRequest;
import com.agrolink.advisory.model.FarmerQuery;
import com.agrolink.advisory.repository.QueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryService {
    private final QueryRepository repository;

    public FarmerQuery submitQuery(QueryRequest req) {
        FarmerQuery query = new FarmerQuery(
                null, req.getFarmerName(), req.getQuestion(),
                LocalDate.now().toString(), null, null);
        return repository.save(query);
    }

    public FarmerQuery respondToQuery(String id, String response) {
        FarmerQuery query = repository.findById(id).orElseThrow();
        query.setAdvisorResponse(response);
        query.setResponseDate(LocalDate.now().toString());
        return repository.save(query);
    }

    public List<FarmerQuery> getAllQueries() {
        return repository.findAll();
    }
}
