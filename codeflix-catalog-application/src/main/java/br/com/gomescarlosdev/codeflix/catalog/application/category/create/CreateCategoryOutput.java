package br.com.gomescarlosdev.codeflix.catalog.application.category.create;

import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;

public record CreateCategoryOutput(String id) {

    public static CreateCategoryOutput from (String id){
        return new CreateCategoryOutput(id);
    }

    public static CreateCategoryOutput from (Category category){
        return new CreateCategoryOutput(category.getId().getValue());
    }

}
