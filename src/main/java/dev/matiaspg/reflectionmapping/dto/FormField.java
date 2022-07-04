package dev.matiaspg.reflectionmapping.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Contains the basic info of a form field.
 * This may be used in the frontend to create a form dynamically depending on
 * the field type, etc.
 * TODO: Add support for validators?
 */
@Data
@Builder
public class FormField {
    private String name;
    private String label;
    private String type;
    private Object value;
}
