package br.com.gomescarlosdev.codeflix.catalog.application.category.update;

import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;

public record UpdateCategoryResponse(String id) {

    public static UpdateCategoryResponse from(Category category) {
        return new UpdateCategoryResponse(category.getId().getValue());
    }

}
