package com.dnd.niceteam.code.config;

import com.dnd.niceteam.domain.code.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnumMapperConfig {

    @Bean
    public EnumMapperFactory enumMapperFactory() {
        EnumMapperFactory enumMapperFactory = new EnumMapperFactory();
        enumMapperFactory.put(DayOfWeek.class.getSimpleName(), DayOfWeek.class);
        enumMapperFactory.put(Field.class.getSimpleName(), Field.class);
        enumMapperFactory.put(FieldCategory.class.getSimpleName(), FieldCategory.class);
        enumMapperFactory.put("PersonalityAdjective", Personality.Adjective.class);
        enumMapperFactory.put("PersonalityNoun", Personality.Noun.class);
        enumMapperFactory.put(ReviewTag.class.getSimpleName(), ReviewTag.class);
        return enumMapperFactory;
    }
}
