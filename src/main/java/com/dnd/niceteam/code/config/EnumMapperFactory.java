package com.dnd.niceteam.code.config;

import com.dnd.niceteam.domain.common.EnumMapperType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumMapperFactory {

    private final Map<String, List<EnumMapperType>> factory = new LinkedHashMap<>();

    public void put(String key, Class<? extends EnumMapperType> e) {
        factory.put(key, toEnumValues(e));
    }

    private List<EnumMapperType> toEnumValues(Class<? extends EnumMapperType> e) {
        return Arrays.stream(e.getEnumConstants())
                .collect(Collectors.toList());
    }

    public List<EnumMapperType> get(String key){
        return factory.getOrDefault(key, Collections.emptyList());
    }

    public Map<String, List<EnumMapperType>> get(List<String> keys) {
        if(keys == null || keys.isEmpty()){
            return new LinkedHashMap<>();
        }
        return keys.stream()
                .collect(Collectors.toMap(Function.identity(), this::get));
    }

    public Map<String, List<EnumMapperType>> getAll() {
        return factory;
    }
}
