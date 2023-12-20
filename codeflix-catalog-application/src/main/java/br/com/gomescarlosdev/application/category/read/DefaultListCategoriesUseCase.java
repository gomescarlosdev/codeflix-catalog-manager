package br.com.gomescarlosdev.application.category.read;

import br.com.gomescarlosdev.domain.category.CategoryGateway;
import br.com.gomescarlosdev.domain.category.CategorySearchQuery;
import br.com.gomescarlosdev.domain.pagination.Pagination;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<CategoryListResponse> execute(CategorySearchQuery query) {
        return this.categoryGateway.findAll(query).map(CategoryListResponse::from);
    }

}
