package com.example.concertsystem.controller;

import com.example.concertsystem.service.firebase.FirebaseService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/img")
public class ImageController {

    private FirebaseService firebaseService;
    public ImageController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @GetMapping("/download")
    public String downloadImage(@RequestParam("fileName") String fileName) throws IOException {
        return firebaseService.download(fileName);
    }
    @GetMapping("/get")
    public ResponseEntity<byte[]> getImage(@RequestParam("fileName") String fileName) throws IOException {
        return firebaseService.getImage(fileName);
    }
    @PostMapping("/")
    public String upload(@RequestParam("file")MultipartFile multipartFile) throws IOException {
        return firebaseService.upload(multipartFile);
    }
}
