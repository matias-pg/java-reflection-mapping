package dev.matiaspg.reflectionmapping.controller;

import dev.matiaspg.reflectionmapping.fieldmapping.MappedFormField;
import dev.matiaspg.reflectionmapping.model.Todo;
import dev.matiaspg.reflectionmapping.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * Get a {@link List} of {@link MappedFormField}s containing the values of a
     * {@link Todo}.
     */
    @GetMapping("/{id}/form-fields")
    public List<MappedFormField> findFormFieldsForId(@PathVariable Integer id) {
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
