package br.com.gomescarlosdev.application.category.create;

import br.com.gomescarlosdev.domain.category.Category;
import br.com.gomescarlosdev.domain.category.CategoryID;

public record CreateCategoryResponse(CategoryID id) {

    public static CreateCategoryResponse from (Category category){
        return new CreateCategoryResponse(category.getId());
    }

}
