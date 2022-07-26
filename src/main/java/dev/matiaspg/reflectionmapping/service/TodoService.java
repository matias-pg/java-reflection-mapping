package dev.matiaspg.reflectionmapping.service;

import dev.matiaspg.reflectionmapping.fieldmapping.MappedFormField;
import dev.matiaspg.reflectionmapping.model.Todo;

import java.util.List;

public interface TodoService {
    List<Todo> findAll();

    Todo findById(Integer id);

    /**
     * Get a {@link List} of {@link MappedFormField}s containing the values of a
     * {@link Todo}.
     */
    List<MappedFormField> findFormFieldsForId(Integer id);

    Todo save(Todo todo);

    void update(Integer id, Todo todo);

    void delete(Integer id);
}
