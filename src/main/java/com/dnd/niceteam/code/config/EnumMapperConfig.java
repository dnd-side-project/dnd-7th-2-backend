package com.dnd.niceteam.code.config;

import com.dnd.niceteam.domain.code.DayOfWeek;
import com.dnd.niceteam.domain.code.Field;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnumMapperConfig {

    @Bean
    public EnumMapperFactory enumMapperFactory() {
        EnumMapperFactory enumMapperFactory = new EnumMapperFactory();
        enumMapperFactory.put(DayOfWeek.class.getSimpleName(), DayOfWeek.class);
        enumMapperFactory.put(Field.class.getSimpleName(), Field.class);
        return enumMapperFactory;
    }
}
