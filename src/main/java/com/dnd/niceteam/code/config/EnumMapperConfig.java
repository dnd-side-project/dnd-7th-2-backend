package com.dnd.niceteam.code.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnumMapperConfig {

    @Bean
    public EnumMapperFactory enumMapperFactory() {
        EnumMapperFactory enumMapperFactory = new EnumMapperFactory();
        return enumMapperFactory;
    }
}
