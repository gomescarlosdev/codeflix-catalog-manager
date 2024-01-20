package br.com.gomescarlosdev.codeflix.catalog.domain.category;

import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.SearchQuery;
import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {

    Category create(Category category);

    Category update(Category category);

    void deleteById(CategoryID categoryID);

    Optional<Category> findById(CategoryID categoryID);

    Pagination<Category> findAll(SearchQuery query);

}
