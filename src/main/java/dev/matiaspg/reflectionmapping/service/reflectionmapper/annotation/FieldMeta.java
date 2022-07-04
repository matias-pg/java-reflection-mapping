package dev.matiaspg.reflectionmapping.service.reflectionmapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Contains metadata of a {@code FormField}.
 * TODO: Add more fields to this annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldMeta {
    /**
     * Label of the field
     */
    String label();
}
