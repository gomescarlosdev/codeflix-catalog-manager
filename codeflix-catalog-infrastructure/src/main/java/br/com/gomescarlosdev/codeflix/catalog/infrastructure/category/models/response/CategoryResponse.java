package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.models.response;

import br.com.gomescarlosdev.codeflix.catalog.application.category.read.GetCategoryResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("isActive") boolean active,
        @JsonProperty("createdAt") Instant createdAt,
        @JsonProperty("updatedAt") Instant updatedAt,
        @JsonProperty("deletedAt") Instant deletedAt
){
    public static CategoryResponse from(GetCategoryResponse category) {
        return new CategoryResponse(
                category.id().getValue(),
                category.name(),
                category.description(),
                category.active(),
                category.createdAt(),
                category.updatedAt(),
                category.deletedAt()
        );
    }
}
