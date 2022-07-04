package dev.matiaspg.reflectionmapping.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import dev.matiaspg.reflectionmapping.utils.reflectionmapper.annotation.FieldMeta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    private Integer id;

    @FieldMeta(label = "Category name")
    private String name;
}
