package br.com.gomescarlosdev.codeflix.catalog.domain.category;

import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {

    Category create(Category category);

    void deleteById(CategoryID categoryID);

    Optional<Category> findById(CategoryID categoryID);

    Category update(Category category);

    Pagination<Category> findAll(CategorySearchQuery query);


}