package com.example.concertsystem.service.firebase;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FirebaseService {
    String upload(MultipartFile multipartFile) throws IOException;
    List<String> addAllImages(List<MultipartFile> images);
    ResponseEntity<byte[]> getImage(String fileName) throws IOException;
    String getImageUrl(String fileName) throws IOException;
    String download(String fileName) throws IOException;
}
