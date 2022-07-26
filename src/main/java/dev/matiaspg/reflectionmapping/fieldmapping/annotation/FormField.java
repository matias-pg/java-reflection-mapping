package dev.matiaspg.reflectionmapping.fieldmapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Annotation to set form metadata to a class {@link Field}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormField {
    /**
     * Label of the field
     */
    String label();

    boolean readOnly() default false;
}
