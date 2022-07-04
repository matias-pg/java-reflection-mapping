package dev.matiaspg.reflectionmapping.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.matiaspg.reflectionmapping.service.reflectionmapper.mapper.FieldMapper;
import dev.matiaspg.reflectionmapping.service.reflectionmapper.mapper.StringMapper;

@Configuration
public class FormFieldMappers {
    @Bean
    public Map<Class<? extends Object>, FieldMapper> mappers() {
        // Supported mappers
        // TODO: Support Number, Boolean, Date, and Lists
        // TODO: Add support for validators?
        return Map.of(
                String.class, StringMapper::map);
    }

}
