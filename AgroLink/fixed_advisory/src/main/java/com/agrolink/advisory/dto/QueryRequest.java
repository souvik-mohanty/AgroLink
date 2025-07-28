package com.agrolink.advisory.dto;

import lombok.Data;

@Data
public class QueryRequest {
    private String farmerName;
    private String question;
}
