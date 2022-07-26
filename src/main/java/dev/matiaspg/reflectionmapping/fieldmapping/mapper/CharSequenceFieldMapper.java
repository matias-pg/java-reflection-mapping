package dev.matiaspg.reflectionmapping.fieldmapping.mapper;

import org.springframework.stereotype.Component;

@Component
public class CharSequenceFieldMapper implements FieldMapper {
    private static final String FIELD_TYPE = "text";

    @Override
    public Class<CharSequence> supportedType() {
        return CharSequence.class;
    }

    @Override
    public String getFieldType() {
        return FIELD_TYPE;
    }
}
