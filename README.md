Map class fields and their value using only annotations. Forget about custom mapping classes!

# The problem

Imagine your project has a lot of entities (50+), where each entity has 10 fields on average, and you have to map each
of those fields (and their value) to a custom structure (like
the [`MappedFormField`](https://github.com/matias-pg/java-reflection-mapping/blob/master/src/main/java/dev/matiaspg/reflectionmapping/fieldmapping/MappedFormField.java)
that we're using in this project).

Well... I had to do that. I had to create a structure that:

- Was able to represent the original structure of an entity (with the value of each field), i.e. could be mapped *back*
  to the original structure
- Could be used in frontends (the functionality would be exposed by a public API) to create dynamic views, which could
  display the data in a structured way (like with tables), as well as allowing to modify the original data (i.e. with
  forms).

So to do that, it was decided to use a structure similar to the following:

```java
@Data
@Builder
public class MappedFormField {
    private String name;
    private String type;
    private Object value;
    private String label;
}
```

# The naive solution

At the time, I didn't think mapping 500+ fields was going to be difficult. I knew it was going
to be a lot of work, but that it wasn't going to be difficult, as mapping the fields was a simple task. Also... I didn't
know how to use the Reflection API from Java.

So... Knowing that, I used the easiest (and the naivest!) approach that came to my mind: create mapping classes!

So for example, if I wanted to map
an [entity](https://github.com/matias-pg/java-reflection-mapping/blob/master/src/main/java/dev/matiaspg/reflectionmapping/model/Todo.java)
like this:

```java
@Entity
public class Todo {
    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category category;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}


@Entity
public class Category {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
```

I would create a mapping class like this:

```java
public class TodoFormFieldMapper {
    public static List<MappedFormField> toFormFields(Todo todo) {
        return List.of(
                numberField("id", todo.getId(), "TODO id"),
                textField("title", todo.getTitle(), "TODO title"),
                textField("description", todo.getDescription(), "TODO description"),
                listField("category",
                        // Use another mapping class for `Category`
                        CategoryFormFieldMapper.toFormFields(todo.getCategory()),
                        "TODO category"),
                dateField("createdAt", todo.getCreatedAt(), "TODO createdAt"),
                dateField("updatedAt", todo.getUpdatedAt(), "TODO updatedAt")
        );
    }
}
```

> Note: `numberField`, `textField`, `listField` and `dateField` were static constructors imported statically
> from `MappedFormField`. They were a shortcut to create a `MappedFormField` with a specific `type`.

Although this approach worked, it had some issues.

First, it was too easy to make a mistake. Considering there were 500+ fields that had to be mapped, it was easy to make
a mistake like this:

```java
public class TodoFormFieldMapper {
    public static List<MappedFormField> toFormFields(Todo todo) {
        return List.of(
                // ...
                // The value is of a different field!
                textField("description", todo.getTitle(), "TODO description")
                // ...
        );
    }
}
```

Even though those mistakes could be easily found with a regular expression like
this: `\w+Field\(\s*"\w([^"]+)",\s*\w+\.get\w(?:(?!\1).)+,`, we shouldn't have to do that, since *it shouldn't* be that
easy to make a mistake.

Second, there's too much code repetition, with all that entails (like the previous issue). We already know the type and
the name of each field of an entity, since we defined them in the entity itself, and we can easily get the value of a
field by using Reflection. The only thing we need is the `label`.

And third, that's a lot of code! Do we really have to create a mapping class for each of those 50+ entities? I did, but
I shouldn't have.

As some say, you should work smarter, not harder. I made the mistake of working harder, so I was curious about the
"smarter" solution. And although it could still be improved, this is my "smarter" solution.

# The proposed solution

To address the issues mentioned earlier, we could use custom annotations and the Reflection API.

Since the only thing we need to map a field is the `label`, we could create a custom annotation that allows us to define
the label of a field, like this:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormField {
    String label();
}
```

So to use it, we could simply annotate the fields we want to map like this:

```java
@Entity
public class Todo {
    @Id
    @GeneratedValue
    @FormField(label = "TODO id") // New
    private Integer id;

    @FormField(label = "TODO title") // New
    private String title;

    @FormField(label = "TODO description") // New
    private String description;

    /**
     * This field will be mapped recursively.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @FormField(label = "TODO category") // New
    private Category category;

    @CreationTimestamp
    @FormField(label = "TODO createdAt") // New
    private Date createdAt;

    @UpdateTimestamp
    @FormField(label = "TODO updatedAt") // New
    private Date updatedAt;
}


@Entity
public class Category {
    @Id
    @GeneratedValue
    @FormField(label = "Category id") // New
    private Integer id;

    @FormField(label = "Category name") // New
    private String name;

    @CreationTimestamp
    @FormField(label = "Category createdAt") // New
    private Date createdAt;

    @UpdateTimestamp
    @FormField(label = "Category updatedAt") // New
    private Date updatedAt;
}
```

And that's it! We no longer need to create any mapping class. And notice that it maps the fields recursively! (In
the `category` field.)

The mapping mechanism is simple, it works like this:

- Step 1: Get all the fields that have the `@FormField` annotation
- Step 2: For each of those fields:
    - Check: Does the field type have a mapper? (
      Like [`Integer`](https://github.com/matias-pg/java-reflection-mapping/blob/master/src/main/java/dev/matiaspg/reflectionmapping/fieldmapping/mapper/NumberFieldMapper.java)
      , [`String`](https://github.com/matias-pg/java-reflection-mapping/blob/master/src/main/java/dev/matiaspg/reflectionmapping/fieldmapping/mapper/CharSequenceFieldMapper.java)
      or [`Date`](https://github.com/matias-pg/java-reflection-mapping/blob/master/src/main/java/dev/matiaspg/reflectionmapping/fieldmapping/mapper/DateFieldMapper.java)
      does)
        - If it does, map the field using that mapper
        - Otherwise, check if the field type (e.g. `Category`) has any annotated field:
            - If it does, go to the Step 2 with those fields (recursion)
            - If it doesn't (e.g. `Map`), use the default mapper, which maps the field and its value to
              a `MappedFormField` that has the type of `object`. The mapped field will have the original value of the
              field, and will delegate the responsibility of serializing it (e.g. to Jackson, the serializer used by
              Spring)

# Going further

You may have noticed that there are fields that should not be editable, like the ID and the timestamps of an entity. To
fix that, we could add a new field to our annotation: the `readOnly` field. The result will be like this:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormField {
    String label();

    boolean readOnly() default false;
}
```

This new field will allow the frontends (remember, the functionality would be exposed by a public API) to render the
form fields in a disabled state.

> Note that this is for the UI only, those fields should be ignored in the backend nonetheless.

So with this change, the entities will end like this:

```java
@Entity
public class Todo {
    @Id
    @GeneratedValue
    @FormField(label = "TODO id", readOnly = true) // Changed
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
    @FormField(label = "TODO createdAt", readOnly = true) // Changed
    private Date createdAt;

    @UpdateTimestamp
    @FormField(label = "TODO updatedAt", readOnly = true) // Changed
    private Date updatedAt;
}


@Entity
public class Category {
    @Id
    @GeneratedValue
    @FormField(label = "Category id", readOnly = true) // Changed
    private Integer id;

    @FormField(label = "Category name")
    private String name;

    @CreationTimestamp
    @FormField(label = "Category createdAt", readOnly = true) // Changed
    private Date createdAt;

    @UpdateTimestamp
    @FormField(label = "Category updatedAt", readOnly = true) // Changed
    private Date updatedAt;
}
```

We could go even further and add support for constraint annotations defined by JSR-380 (AKA Bean Validation 2.0),
like:

- `@NotBlank`
- `@NotEmpty`
- `@Min` and `@Max`
- `@Size`
- `@Email`
- ...[etc](https://www.baeldung.com/javax-validation#validation)

That's for the future though :). Although I haven't though about it thoroughly, I think It could be implemented similar
to the way Angular reports validation errors, i.e. creating an object where the keys are the constraint "identifiers",
and the values are the constraint options (or `true` if the constraint doesn't have options).

An example of that could be something this:

```json
{
    "notBlank": true,
    "notEmpty": true,
    "min": 18,
    "max": 150,
    "size": {
        "min": 10,
        "max": 200
    },
    "email": true
}
```