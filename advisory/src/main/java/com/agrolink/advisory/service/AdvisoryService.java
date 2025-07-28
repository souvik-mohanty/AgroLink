package com.agrolink.advisory.service;

import com.agrolink.advisory.dto.AdvisoryContentRequest;
import com.agrolink.advisory.model.AdvisoryContent;
import com.agrolink.advisory.repository.AdvisoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvisoryService {
    private final AdvisoryRepository repository;

    public AdvisoryContent postContent(AdvisoryContentRequest req) {
        AdvisoryContent content = new AdvisoryContent(
                null, req.getAdvisorName(), req.getType(), req.getTitle(),
                req.getContent(), LocalDate.now().toString());
        return repository.save(content);
    }

    public List<AdvisoryContent> getAllContent() {
        return repository.findAll();
    }
}
