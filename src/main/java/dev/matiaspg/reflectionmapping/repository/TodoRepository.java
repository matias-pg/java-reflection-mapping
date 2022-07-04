package dev.matiaspg.reflectionmapping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.matiaspg.reflectionmapping.model.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
}
