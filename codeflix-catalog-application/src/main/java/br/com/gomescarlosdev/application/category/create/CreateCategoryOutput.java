package br.com.gomescarlosdev.application.category.create;

import br.com.gomescarlosdev.domain.category.Category;
import br.com.gomescarlosdev.domain.category.CategoryID;

public record CreateCategoryOutput(CategoryID id) {

    public static CreateCategoryOutput from (Category category){
        return new CreateCategoryOutput(category.getId());
    }

}
