package br.com.gomescarlosdev.codeflix.catalog.application.category.read.list;

import br.com.gomescarlosdev.codeflix.catalog.application.category.read.CategoryOutput;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryGateway;
import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.Pagination;
import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.SearchQuery;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<CategoryOutput> execute(SearchQuery query) {
        return this.categoryGateway.findAll(query).map(CategoryOutput::from);
    }

}
