package com.dnd.niceteam.common.jackson;

import com.dnd.niceteam.domain.common.EnumMapperType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class RestDocsObjectMapper extends ObjectMapper {

    public RestDocsObjectMapper() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(EnumMapperType.class, new TestEnumMapperSerializer());

        this.registerModule(module);
        this.registerModule(new JavaTimeModule());
        this.registerModule(new ParameterNamesModule());
        this.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
    }

}
