//package com.swh.medicine.global.fcm;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.messaging.FirebaseMessaging;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Arrays;
//
//@Service
//public class FCMInitializer {
//
//    @Value("${project.properties.firebase-create-scoped}")
//    String fireBaseCreateScoped;
//
//    private FirebaseMessaging instance;
//
//    @PostConstruct
//    public void initialize() throws IOException {
//
//        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource("firebase/fortunecookie-88d3e-firebase-adminsdk-84uy6-acc1066e57.json").getInputStream())
//                .createScoped((Arrays.asList(fireBaseCreateScoped)));
//        FirebaseOptions secondaryAppConfig = FirebaseOptions.builder()
//                .setCredentials(googleCredentials)
//                .build();
//        FirebaseApp app = FirebaseApp.initializeApp(secondaryAppConfig);
//        this.instance = FirebaseMessaging.getInstance(app);
//    }
//}