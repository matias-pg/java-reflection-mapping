package dev.matiaspg.reflectionmapping.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import dev.matiaspg.reflectionmapping.service.reflectionmapper.annotation.FieldMeta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    @GeneratedValue
    private Integer id;

    @FieldMeta(label = "TODO title")
    private String title;

    @FieldMeta(label = "TODO description")
    private String description;

    @FieldMeta(label = "TODO category")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category category;
}
