package com.agrolink.admin.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class Report {
    @Id
    private String id;
    private String type; // PDF or EXCEL
    private Date generatedOn;
    private String downloadUrl;
}

