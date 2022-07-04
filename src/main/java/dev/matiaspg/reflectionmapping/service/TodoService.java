package dev.matiaspg.reflectionmapping.service;

import java.util.List;

import dev.matiaspg.reflectionmapping.dto.FormField;
import dev.matiaspg.reflectionmapping.model.Todo;

public interface TodoService {
    List<Todo> findAll();

    Todo findById(Integer id);

    /**
     * Get a {@code List} of {@code FormFields} containing the values of a
     * {@code Todo}.
     */
    List<FormField> findFormFieldsForId(Integer id);

    Todo save(Todo todo);

    void update(Integer id, Todo todo);

    void delete(Integer id);
}
