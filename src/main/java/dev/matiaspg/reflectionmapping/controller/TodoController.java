package dev.matiaspg.reflectionmapping.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.matiaspg.reflectionmapping.dto.FormField;
import dev.matiaspg.reflectionmapping.model.Todo;
import dev.matiaspg.reflectionmapping.service.TodoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService service;

    @GetMapping
    public List<Todo> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Todo findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    /**
     * Get a {@code List} of {@code FormFields} containing the values of a
     * {@code Todo}.
     */
    @GetMapping("/{id}/form-fields")
    public List<FormField> findFormFieldsForId(@PathVariable Integer id) {
        return service.findFormFieldsForId(id);
    }

    @PostMapping
    public Todo save(@RequestBody Todo todo) {
        return service.save(todo);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Integer id, @RequestBody Todo todo) {
        service.update(id, todo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
