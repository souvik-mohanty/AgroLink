package com.AgroLink.product.service;

import com.AgroLink.product.model.Photo;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface PhotoService {
    public String addPhoto(MultipartFile file, String uploaderId) throws IOException;
    public Photo getPhoto(String id);

    void deletePhoto(String s);
}
