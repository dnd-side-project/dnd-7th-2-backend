package com.dnd.niceteam.notification.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("firebase")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class FirebaseProperties implements Serializable {

    private final String type;
    private final String projectId;

    private final String privateKeyId;
    private final String privateKey;

    private final String clientEmail;
    private final String clientId;

    private final String authUri;
    private final String tokenUri;

    private final String authProviderX509CertUrl;
    private final String clientX509CertUrl;

    public InputStream toStream() throws JsonProcessingException {
        String serializedProperties = new ObjectMapper().writeValueAsString(this).replaceAll("\\\\n", "\\n");
        return new ByteArrayInputStream(serializedProperties.getBytes());
    }

}
