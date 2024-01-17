package br.com.gomescarlosdev.codeflix.catalog.application.category.update;

import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;

public record UpdateCategoryOutput(String id) {

    public static UpdateCategoryOutput from(Category category) {
        return new UpdateCategoryOutput(category.getId().getValue());
    }

    public static UpdateCategoryOutput from(String category) {
        return new UpdateCategoryOutput(category);
    }

}
