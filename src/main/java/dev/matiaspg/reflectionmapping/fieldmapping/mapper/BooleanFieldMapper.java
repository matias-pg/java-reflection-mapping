package dev.matiaspg.reflectionmapping.fieldmapping.mapper;

import org.springframework.stereotype.Component;

@Component
public class BooleanFieldMapper implements FieldMapper {
    private static final String FIELD_TYPE = "boolean";

    @Override
    public Class<Boolean> supportedType() {
        return Boolean.class;
    }

    @Override
    public String getFieldType() {
        return FIELD_TYPE;
    }
}
