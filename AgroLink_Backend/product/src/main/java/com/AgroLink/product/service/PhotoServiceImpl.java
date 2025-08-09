package com.AgroLink.product.service;


import com.AgroLink.product.exception.PhotoNotFoundException;
import com.AgroLink.product.model.Photo;
import com.AgroLink.product.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service // The @Service annotation goes on the implementation class
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    @Override
    public String addPhoto(MultipartFile file, String uploaderId) throws IOException {
        Photo photo = Photo.builder()
                .uploaderId(uploaderId)
                .filename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .imageData(file.getBytes())
                .build();

        Photo savedPhoto = photoRepository.save(photo);
        return savedPhoto.getId();
    }

    @Override
    public Photo getPhoto(String id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new PhotoNotFoundException("Photo not found with id: " + id));
    }

    @Override
    public void deletePhoto(String id) {
        Photo photo = getPhoto(id); // This will throw PhotoNotFoundException if not found
        photoRepository.delete(photo);
    }
}