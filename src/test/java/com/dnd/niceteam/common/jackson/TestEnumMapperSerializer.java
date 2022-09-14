package com.dnd.niceteam.common.jackson;

import com.dnd.niceteam.domain.common.EnumMapperType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class TestEnumMapperSerializer extends StdSerializer<EnumMapperType> {

    public TestEnumMapperSerializer() {
        super(EnumMapperType.class);
    }

    @Override
    public void serialize(EnumMapperType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("code", value.getCode());
        gen.writeStringField("title", value.getTitle());
        gen.writeEndObject();
    }

}
