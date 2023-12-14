package br.com.gomescarlosdev.application.category.update;

import br.com.gomescarlosdev.domain.category.Category;

public record UpdateCategoryResponse(String id) {

    public static UpdateCategoryResponse from(Category category) {
        return new UpdateCategoryResponse(category.getId().getValue());
    }

}
