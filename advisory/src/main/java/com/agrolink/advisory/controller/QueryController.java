package com.agrolink.advisory.controller;

import com.agrolink.advisory.dto.QueryRequest;
import com.agrolink.advisory.model.FarmerQuery;
import com.agrolink.advisory.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/query")
@RequiredArgsConstructor
public class QueryController {

    private final QueryService queryService;

    @PostMapping("/submit")
    public FarmerQuery submitQuery(@RequestBody QueryRequest request) {
        return queryService.submitQuery(request);
    }

    @PostMapping("/respond/{id}")
    public FarmerQuery respondToQuery(@PathVariable String id, @RequestBody String response) {
        return queryService.respondToQuery(id, response);
    }

    @GetMapping("/all")
    public List<FarmerQuery> getAllQueries() {
        return queryService.getAllQueries();
    }
}
