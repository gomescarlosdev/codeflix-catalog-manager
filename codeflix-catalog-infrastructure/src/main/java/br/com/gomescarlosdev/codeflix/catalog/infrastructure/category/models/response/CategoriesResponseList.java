package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.models.response;

import br.com.gomescarlosdev.codeflix.catalog.application.category.read.CategoryOutput;
import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.Pagination;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoriesResponseList(
        @JsonProperty("categories") Pagination<CategoryResponse> categories
) {

    public static CategoriesResponseList from(Pagination<CategoryOutput> categories) {
        return new CategoriesResponseList(
                new Pagination<>(
                        categories.page(),
                        categories.offset(),
                        categories.total(),
                        categories.items().stream().map(CategoryResponse::from).toList()
                ));
    }

}
