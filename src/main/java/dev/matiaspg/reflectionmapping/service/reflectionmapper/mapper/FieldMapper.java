package dev.matiaspg.reflectionmapping.service.reflectionmapper.mapper;

import java.lang.reflect.Field;

import dev.matiaspg.reflectionmapping.dto.FormField;
import dev.matiaspg.reflectionmapping.service.reflectionmapper.annotation.FieldMeta;

/**
 * Represents a mapper that maps a class {@code Field} into a {@code FormField}.
 */
@FunctionalInterface
public interface FieldMapper {
    /**
     * Maps a {@code Field} into a {@code FormField}.
     * <p>
     * Note: I'm using the current method signature to reduce code repetition
     * inside mappers, but it could also be
     * {@code map(Class<? extends Object>, Field)}, extracting the value and
     * metadata inside the mapper.
     *
     * @param value The value of the {@code Field}
     * @param field The {@code Field} to map
     * @param meta  The metadata of the {@code Field}
     * @return The FormField
     */
    FormField map(Object value, Field field, FieldMeta meta);
}
