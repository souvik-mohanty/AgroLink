package com.agrolink.admin.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Complaint {
    @Id
    private String id;
    private String raisedBy;
    private String against;
    private String message;
    private boolean resolved;
}

