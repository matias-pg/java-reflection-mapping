package dev.matiaspg.reflectionmapping.listener;

import dev.matiaspg.reflectionmapping.model.Category;
import dev.matiaspg.reflectionmapping.model.Todo;
import dev.matiaspg.reflectionmapping.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApplicationReadyEventListener {
    private final TodoService todoService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent() {
        log.info("Populating the database with TODOs");

        for (int i = 1; i <= 10; i++) {
            // Creates a series of true, false, null, false, true, null, ...
            // This is to demonstrate that null values are handled correctly
            Boolean enabled = i % 3 == 0 ? null : i % 2 != 0;

            var category = Category.builder()
                    .name("Fake Category " + i)
                    .enabled(enabled)
                    .build();

            var todo = Todo.builder()
                    .title("Fake TODO " + i)
                    .description("Fake TODO description. Lorem ipsum dolor sit amet " + 1)
                    .category(category)
                    .build();

            todoService.save(todo);
        }
    }
}
