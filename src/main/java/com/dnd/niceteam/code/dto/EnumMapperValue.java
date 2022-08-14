package com.dnd.niceteam.code.dto;


import com.dnd.niceteam.domain.common.EnumMapperType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class EnumMapperValue {

    private final String code;
    private final String title;

    public EnumMapperValue(EnumMapperType enumMapperType) {
        this.code = enumMapperType.getCode();
        this.title = enumMapperType.getTitle();
    }
}
