package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateCategoryRequest(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("isActive") boolean active
) { }
