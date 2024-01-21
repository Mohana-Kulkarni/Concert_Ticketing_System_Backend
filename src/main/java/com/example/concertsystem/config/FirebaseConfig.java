//package com.example.concertsystem.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Objects;
//
//@Configuration
//public class FirebaseConfig {
//
//    @Bean
//    public Object initializeFirebase() throws IOException {
//        try{
//            FileInputStream serviceAccount = new FileInputStream("concert-ticketing-system-67922-firebase-adminsdk-66i0l-ab05e09b6c.json");
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(Objects.requireNonNull(serviceAccount)))
//                    .setStorageBucket("concert-ticketing-system-67922.appspot.com")
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return new Object();
//    }
//}
