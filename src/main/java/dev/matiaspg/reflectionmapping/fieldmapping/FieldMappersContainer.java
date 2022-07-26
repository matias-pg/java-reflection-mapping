package dev.matiaspg.reflectionmapping.fieldmapping;

import dev.matiaspg.reflectionmapping.fieldmapping.mapper.FieldMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class FieldMappersContainer {
    private final Collection<FieldMapper> fieldMappers;

    // TODO: Cache the mappers for each field type

    /**
     * Gets a {@link FieldMapper} that supports a field type, or a default one
     * if no mapper was found that supports that type.
     *
     * @param fieldType     The field type for which a mapper will be retrieved
     * @param defaultMapper The default mapper
     * @return The {@link FieldMapper}
     */
    public FieldMapper getForWithDefault(Class<?> fieldType, FieldMapper defaultMapper) {
        return fieldMappers
                .stream()
                .filter(m -> m.supports(fieldType))
                .findFirst()
                .orElse(defaultMapper);
    }
}
