package com.agrolink.advisory.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "farmer_queries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmerQuery {
    @Id
    private String id;
    private String farmerName;
    private String question;
    private String submittedDate;
    private String advisorResponse;
    private String responseDate;
}
