package br.com.gomescarlosdev.domain.category;

import br.com.gomescarlosdev.domain.AggregateRoot;
import br.com.gomescarlosdev.domain.validation.ValidationHandler;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;


@Getter
@EqualsAndHashCode(callSuper = false)
public class Category extends AggregateRoot<CategoryID> {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID id,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    @Override
    public void validate(final ValidationHandler handler){
        new CategoryValidator(this, handler).validate();
    }

    public static Category newCategory(
            String name,
            String description,
            boolean active
    ) {
        var now = Instant.now();
        return new Category(
                CategoryID.unique(),
                name,
                description,
                active,
                now,
                now,
                active ? null : now
        );
    }

    public Category deactivate() {
        if(getDeletedAt() == null){
            this.deletedAt = Instant.now();
        }
        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category update(
            String name,
            String description,
            boolean active
    ) {
        if(active)
            activate();
        else
            deactivate();
        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();
        return this;
    }
}