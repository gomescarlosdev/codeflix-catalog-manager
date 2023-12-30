package br.com.gomescarlosdev.codeflix.catalog.application.category.create;

import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryID;

public record CreateCategoryResponse(CategoryID id) {

    public static CreateCategoryResponse from (Category category){
        return new CreateCategoryResponse(category.getId());
    }

}
