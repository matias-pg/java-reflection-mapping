package dev.matiaspg.reflectionmapping.model;

import dev.matiaspg.reflectionmapping.fieldmapping.annotation.FormField;
import dev.matiaspg.reflectionmapping.listener.ApplicationReadyEventListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    @FormField(label = "Category id", readOnly = true)
    private Integer id;

    @FormField(label = "Category name")
    private String name;

    /**
     * Used to demonstrate that it works with null values too.
     *
     * @see ApplicationReadyEventListener#onApplicationEvent()
     */
    @FormField(label = "Category enabled")
    private Boolean enabled;

    @CreationTimestamp
    @FormField(label = "Category createdAt", readOnly = true)
    private Date createdAt;

    @UpdateTimestamp
    @FormField(label = "Category updatedAt", readOnly = true)
    private Date updatedAt;
}
