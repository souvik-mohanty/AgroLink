package com.agrolink.advisory.controller;

import com.agrolink.advisory.dto.AdvisoryContentRequest;
import com.agrolink.advisory.model.AdvisoryContent;
import com.agrolink.advisory.service.AdvisoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advisory")
@RequiredArgsConstructor
public class AdvisoryController {

    private final AdvisoryService advisoryService;

    @PostMapping("/post")
    public AdvisoryContent postContent(@RequestBody AdvisoryContentRequest request) {
        return advisoryService.postContent(request);
    }

    @GetMapping("/all")
    public List<AdvisoryContent> getAll() {
        return advisoryService.getAllContent();
    }
}
