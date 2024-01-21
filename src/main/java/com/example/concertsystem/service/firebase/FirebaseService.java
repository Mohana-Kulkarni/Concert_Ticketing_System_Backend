package com.example.concertsystem.service.firebase;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FirebaseService {
    String upload(MultipartFile multipartFile) throws IOException;

    String download(String fileName) throws IOException;
}
