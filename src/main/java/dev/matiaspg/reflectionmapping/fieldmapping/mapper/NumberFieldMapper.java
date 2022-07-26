package dev.matiaspg.reflectionmapping.fieldmapping.mapper;

import org.springframework.stereotype.Component;

@Component
public class NumberFieldMapper implements FieldMapper {
    private static final String FIELD_TYPE = "number";

    @Override
    public Class<Number> supportedType() {
        return Number.class;
    }

    @Override
    public String getFieldType() {
        return FIELD_TYPE;
    }
}
