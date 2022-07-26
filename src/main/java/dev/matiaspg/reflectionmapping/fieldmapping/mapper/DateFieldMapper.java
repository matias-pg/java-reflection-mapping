package dev.matiaspg.reflectionmapping.fieldmapping.mapper;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateFieldMapper implements FieldMapper {
    private static final String FIELD_TYPE = "date";

    @Override
    public Class<Date> supportedType() {
        return Date.class;
    }

    @Override
    public String getFieldType() {
        return FIELD_TYPE;
    }
}
