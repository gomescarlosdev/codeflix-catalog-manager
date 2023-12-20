package br.com.gomescarlosdev.application.category.read;

import br.com.gomescarlosdev.domain.category.Category;
import br.com.gomescarlosdev.domain.category.CategoryID;

import java.time.Instant;

public record CategoryListResponse(
        CategoryID id,
        String name,
        String description,
        boolean active,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static CategoryListResponse from(Category category) {
        return new CategoryListResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }

}
