package dev.matiaspg.reflectionmapping.fieldmapping.mapper;

import dev.matiaspg.reflectionmapping.fieldmapping.MappedFormField;
import dev.matiaspg.reflectionmapping.fieldmapping.annotation.FormField;

import java.lang.reflect.Field;

/**
 * Represents a function that maps a {@link FormField} with its value into a
 * {@link MappedFormField}.
 * <p>
 * TODO: Make this generic, but without using too many suppressions! (I have a local copy with a lot of them)
 */
public interface FieldMapper {
    Class<?> supportedType();

    default boolean supports(Class<?> fieldType) {
        return supportedType().isAssignableFrom(fieldType);
    }

    String getFieldType();

    /**
     * Maps a {@link Field} with its value to a {@link MappedFormField}.
     *
     * @param value     The value of the {@link Field}
     * @param field     The {@link Field} to map
     * @param formField The metadata of the {@link Field}
     * @return The FormField
     */
    default MappedFormField map(Object value, Field field, FormField formField) {
        return MappedFormField.builder()
                .name(field.getName())
                .type(getFieldType())
                .readOnly(formField.readOnly())
                .label(formField.label())
                .value(value)
                .build();
    }
}
