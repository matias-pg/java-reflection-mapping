package dev.matiaspg.reflectionmapping.service;

import dev.matiaspg.reflectionmapping.exception.NotFoundException;
import dev.matiaspg.reflectionmapping.fieldmapping.FormFieldMapper;
import dev.matiaspg.reflectionmapping.fieldmapping.MappedFormField;
import dev.matiaspg.reflectionmapping.model.Todo;
import dev.matiaspg.reflectionmapping.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository repository;
    private final FormFieldMapper formFieldMapper;

    @Override
    public List<Todo> findAll() {
        return repository.findAll();
    }

    @Override
    public Todo findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("TODO not found"));
    }

    @Override
    public List<MappedFormField> findFormFieldsForId(Integer id) {
        var todo = findById(id);
        return formFieldMapper.toFormFields(todo);
    }

    @Override
    public Todo save(Todo todo) {
        return repository.save(todo);
    }

    @Override
    public void update(Integer id, Todo todo) {
        assertExists(id);
        todo.setId(id);
        repository.save(todo);
    }

    @Override
    public void delete(Integer id) {
        assertExists(id);
        repository.deleteById(id);
    }

    private void assertExists(Integer id) {
        var exists = repository.existsById(id);
        if (!exists) {
            throw new NotFoundException("TODO not found");
        }
    }
}
