package dev.matiaspg.reflectionmapping.utils.reflectionmapper;

import java.util.List;

import dev.matiaspg.reflectionmapping.dto.FormField;

public interface FormFieldMapperService {
    <T> List<FormField> toFormFields(T object);
}
