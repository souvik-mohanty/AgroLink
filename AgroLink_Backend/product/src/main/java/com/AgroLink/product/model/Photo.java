package com.AgroLink.product.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Document("photos")
public class Photo {
    @Id
    private String id;

    // Reference to the user/farmer who uploaded the photo
    private String uploaderId;

    private String filename;
    private String contentType;
    private long fileSize; // Store the file size in bytes
    private String altText; // For accessibility (alt attribute in HTML)

    @CreatedDate
    private Instant uploadDate; // Automatically set on creation

    @LastModifiedDate
    private Instant lastModifiedDate; // Automatically set on update

    private byte[] imageData;
}