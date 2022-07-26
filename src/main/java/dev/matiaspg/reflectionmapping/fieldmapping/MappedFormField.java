package dev.matiaspg.reflectionmapping.fieldmapping;

import lombok.Builder;
import lombok.Data;

/**
 * Contains the basic info of a form field.
 * This could be used in the frontend to create a form dynamically depending on
 * the field type, for example.
 * <p>
 * TODO: Make this generic
 * TODO: (Idea) Add support for validators?
 */
@Data
@Builder
public class MappedFormField {
    private String name;
    private String type;
    private boolean readOnly;
    private String label;
    private Object value;
}
