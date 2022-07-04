package dev.matiaspg.reflectionmapping.service.reflectionmapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import dev.matiaspg.reflectionmapping.dto.FormField;
import dev.matiaspg.reflectionmapping.service.reflectionmapper.annotation.FieldMeta;
import dev.matiaspg.reflectionmapping.service.reflectionmapper.mapper.FieldMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class FormFieldMapperServiceImpl implements FormFieldMapperService {
    /**
     * Map containing a Mapper for a Class.
     * For {@code String}s may be mapped with {@code StringMapper}.
     */
    private final Map<Class<? extends Object>, FieldMapper> mappers;

    @Override
    public List<FormField> toFormFields(Object object) {
        // If we are trying to map a null value, return an empty list
        if (Objects.isNull(object)) {
            return Collections.emptyList();
        }

        // Get the class of the object using reflection
        Class<? extends Object> clazz = object.getClass();

        return Arrays.stream(clazz.getDeclaredFields())
                // Process only fields that were annotated with our annotation
                .filter(field -> field.isAnnotationPresent(FieldMeta.class))

                // Map the Field to a FormField
                .map(field -> mapField(field, object))

                // Ignore fields that couldn't be mapped
                .filter(Objects::nonNull)

                // Get the results as a List
                .collect(Collectors.toList());
    }

    private FormField mapField(Field field, Object object) {
        // Get the annotation of this field
        var meta = field.getAnnotation(FieldMeta.class);

        // Make it accessible, since most fields will be private
        field.setAccessible(true);

        // Get a mapper for the field
        FieldMapper mapper = findMapperFor(field);

        try {
            // Map the value
            var value = field.get(object);
            return mapper.map(value, field, meta);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            log.error("Map error: {}", ex);
            // If it fails, ignore this field
            return null;
        }
    }

    private FieldMapper findMapperFor(Field field) {
        // Loop over the mappers Map
        return mappers.entrySet().stream()
                // Get the entry whose key is the same as the type of the Field
                .filter(e -> e.getKey().equals(field.getType())).findFirst()
                // Get the mapper, i.e. the value of the Map entry
                .map(e -> e.getValue())
                // If no mapper was found, use the default one
                .orElse(this::defaultMapper);
    }

    private FormField defaultMapper(Object object, Field field, FieldMeta meta) {
        // Try to map the field recursively
        var value = toFormFields(object);

        return FormField.builder()
                .name(field.getName())
                .type("object")
                .label(meta.label())
                .value(value)
                .build();
    }
}
