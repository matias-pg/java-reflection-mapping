package dev.matiaspg.reflectionmapping.utils.reflectionmapper.mapper;

import java.lang.reflect.Field;

import dev.matiaspg.reflectionmapping.dto.FormField;
import dev.matiaspg.reflectionmapping.utils.reflectionmapper.annotation.FieldMeta;

public class StringMapper {
    /**
     * Maps a String field into a FormField.
     */
    public static FormField map(Object value, Field field, FieldMeta meta) {
        return FormField.builder()
                .name(field.getName())
                .type("text")
                .label(meta.label())
                .value((String) value)
                .build();
    }
}
