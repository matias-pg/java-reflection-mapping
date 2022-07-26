package dev.matiaspg.reflectionmapping.model;

import dev.matiaspg.reflectionmapping.fieldmapping.annotation.FormField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    @GeneratedValue
    @FormField(label = "TODO id", readOnly = true)
    private Integer id;

    @FormField(label = "TODO title")
    private String title;

    @FormField(label = "TODO description")
    private String description;

    /**
     * This field will be mapped recursively.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @FormField(label = "TODO category")
    private Category category;

    @CreationTimestamp
    @FormField(label = "TODO createdAt", readOnly = true)
    private Date createdAt;

    @UpdateTimestamp
    @FormField(label = "TODO updatedAt", readOnly = true)
    private Date updatedAt;
}
