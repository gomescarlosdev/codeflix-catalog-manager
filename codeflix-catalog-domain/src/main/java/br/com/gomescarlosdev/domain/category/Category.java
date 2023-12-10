package br.com.gomescarlosdev.domain.category;

import br.com.gomescarlosdev.domain.AggregateRoot;
import br.com.gomescarlosdev.domain.validation.ValidationHandler;
import lombok.Getter;

import java.time.Instant;


@Getter
public class Category extends AggregateRoot<CategoryID> {

    private String name;
    private String description;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID id,
            final String name,
            final String description,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(id);
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    @Override
    public void validate(final ValidationHandler handler){
        new CategoryValidator(this, handler).validate();
    }

    public static Category newCategory(
            String expectedName,
            String expectedDescription,
            boolean expectedIsActive
    ) {
        var now = Instant.now();
        return new Category(
                CategoryID.unique(),
                expectedName,
                expectedDescription,
                expectedIsActive,
                now,
                now,
                null
        );
    }
}
