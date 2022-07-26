package dev.matiaspg.reflectionmapping.fieldmapping;

import dev.matiaspg.reflectionmapping.fieldmapping.annotation.FormField;
import dev.matiaspg.reflectionmapping.fieldmapping.mapper.FieldMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
public class FormFieldMapper {
    private final DefaultFieldMapper DEFAULT_MAPPER = new DefaultFieldMapper();
    private final FieldMappersContainer fieldMappersContainer;

    public List<MappedFormField> toFormFields(Object object) {
        // If we are trying to map a null value, return an empty list
        if (Objects.isNull(object)) {
            return Collections.emptyList();
        }

        // Get the class of the object using reflection
        Class<?> clazz = object.getClass();

        // Map every mappeable field of the class
        return getMappeableFieldsFrom(clazz)

                // Map the Field to a FormField
                .map(field -> mapField(field, object))

                // Ignore fields that couldn't be mapped
                .filter(Objects::nonNull)

                // Get the results as a List
                .collect(Collectors.toList());
    }

    private Stream<Field> getMappeableFieldsFrom(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                // Process only fields that were annotated with our annotation
                .filter(field -> field.isAnnotationPresent(FormField.class));
    }

    private MappedFormField mapField(Field field, Object object) {
        // Get the annotation of this field
        FormField formField = field.getAnnotation(FormField.class);

        // Make it accessible, since most fields will be private
        field.setAccessible(true);

        Class<?> fieldType = field.getType();

        try {
            Object value = field.get(object);

            // Get a mapper for the field
            FieldMapper mapper = fieldMappersContainer.getForWithDefault(fieldType, DEFAULT_MAPPER);

            // Map the value
            return mapper.map(value, field, formField);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            log.error("Field mapping error", ex);
            // If it fails, ignore this field
            return null;
        }
    }

    private class DefaultFieldMapper implements FieldMapper {
        private static final String FIELD_TYPE = "object";

        @Override
        public Class<Object> supportedType() {
            return Object.class;
        }

        @Override
        public String getFieldType() {
            return FIELD_TYPE;
        }

        @Override
        public MappedFormField map(Object value, Field field, FormField formField) {
            if (value == null) {
                return null;
            }

            boolean hasMappeableFields = getMappeableFieldsFrom(value.getClass()).findFirst().isPresent();

            // Map the field recursively if it has mappeable fields
            var mappedFieldsOrValue = hasMappeableFields ? toFormFields(value) : value;

            return MappedFormField.builder()
                    .name(field.getName())
                    .type(getFieldType())
                    .readOnly(formField.readOnly())
                    .label(formField.label())
                    .value(mappedFieldsOrValue)
                    .build();
        }
    }
}
