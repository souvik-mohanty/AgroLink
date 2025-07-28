package com.agrolink.advisory.dto;

import lombok.Data;

@Data
public class AdvisoryContentRequest {
    private String advisorName;
    private String type; // blog, tip, video
    private String title;
    private String content;
}
