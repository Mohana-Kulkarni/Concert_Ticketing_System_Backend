package com.example.concertsystem.service.firebase;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FirebaseServiceImpl implements FirebaseService {

    private Storage storage;

    public FirebaseServiceImpl() {

        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
            file.delete();
            return URL;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
    }

    @Override
    public ResponseEntity<byte[]> getImage(String fileName) throws IOException {
        String destFileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));
//        InputStream inputStream = FirebaseService.class.getClassLoader().getResourceAsStream("concert-ticketing-system-67922-firebase-adminsdk-66i0l-ab05e09b6c.json");
//        Credentials credentials = GoogleCredentials.fromStream(inputStream);
//        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = getBlobForImage(fileName);
        byte[] content= blob.getContent();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity.ok().headers(headers).body(content);
    }

    @Override
    public String getImageUrl(String fileName) throws IOException {
        String bucketName = "concert-ticketing-system-67922.appspot.com";
        String encodedBucketName = URLEncoder.encode(bucketName, StandardCharsets.UTF_8);
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", encodedBucketName, encodedFileName);
    }

    @Override
    public String download(String fileName) throws IOException {
        String destFileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));
        String destFilePath = "D:\\" + destFileName;

        Blob blob = getBlobForImage(fileName);
//        InputStream inputStream = FirebaseService.class.getClassLoader().getResourceAsStream("concert-ticketing-system-67922-firebase-adminsdk-66i0l-ab05e09b6c.json");
//        Credentials credentials = GoogleCredentials.fromStream(inputStream);
//        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
//        Blob blob = storage.get(BlobId.of("concert-ticketing-system-67922.appspot.com", fileName));
        blob.downloadTo(Paths.get(destFilePath));

        return "Image downloaded successfully : " + fileName;
    }

    private Blob getBlobForImage(String fileName) throws IOException {
        String bucketName = "concert-ticketing-system-67922.appspot.com";
        InputStream inputStream = FirebaseService.class.getClassLoader().getResourceAsStream("concert-ticketing-system-67922-firebase-adminsdk-66i0l-ab05e09b6c.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        return blob;
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("concert-ticketing-system-67922.appspot.com", fileName); // Replace with your bucker name
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        InputStream inputStream = FirebaseService.class.getClassLoader().getResourceAsStream("concert-ticketing-system-67922-firebase-adminsdk-66i0l-ab05e09b6c.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/concert-ticketing-system-67922.appspot.com/o";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


}

