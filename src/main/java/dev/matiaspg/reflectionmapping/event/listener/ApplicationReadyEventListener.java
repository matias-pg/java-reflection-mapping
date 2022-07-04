package dev.matiaspg.reflectionmapping.event.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import dev.matiaspg.reflectionmapping.model.Category;
import dev.matiaspg.reflectionmapping.model.Todo;
import dev.matiaspg.reflectionmapping.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationReadyEventListener {
    private final TodoService todoService;

    @EventListener
    public void handleContextStart(ApplicationReadyEvent event) {
        log.info("Populating the database with TODOs");

        for (int i = 0; i < 5; i++) {
            var todo = Todo.builder()
                    .title("Fake TODO " + i)
                    .description("Fake TODO description. Lorem ipsum dolor sit amet " + 1)
                    .category(Category.builder().name("Example" + i).build())
                    .build();

            todoService.save(todo);
        }
    }
}
