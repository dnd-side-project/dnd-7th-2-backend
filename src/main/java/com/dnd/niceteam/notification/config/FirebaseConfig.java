package com.dnd.niceteam.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class FirebaseConfig {

    private final String configPath;

    public FirebaseConfig(@Value("${firebase.config-path}") String configPath) {
        this.configPath = configPath;
    }

    @PostConstruct
    public void init() {
        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(configPath).getInputStream()))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("FirebaseApp has been initialized");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


