package com.dnd.niceteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NiceteamBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NiceteamBackendApplication.class, args);
    }

}
