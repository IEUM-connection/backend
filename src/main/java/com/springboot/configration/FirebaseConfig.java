package com.springboot.configration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

@Slf4j
@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void initialize() {
//        try {
//
//            URL resource = getClass().getClassLoader().getResource("firebase-service-account.json");
//            String filePath = resource.getFile();
//
//            FileInputStream serviceAccount =
//                    new FileInputStream(filePath);
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//
//            if (FirebaseApp.getApps().stream().count() <= 0) {
//                FirebaseApp.initializeApp(options);
//            }
//
//        } catch (FileNotFoundException e) {
//            log.error("Firebase ServiceAccountKey FileNotFoundException" + e.getMessage());
//        } catch (IOException e) {
//            log.error("FirebaseOptions IOException" + e.getMessage());
//        }
    }
}
