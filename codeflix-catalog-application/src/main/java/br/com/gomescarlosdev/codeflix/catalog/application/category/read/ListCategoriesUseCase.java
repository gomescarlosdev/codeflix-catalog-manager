package br.com.gomescarlosdev.codeflix.catalog.application.category.read;

import br.com.gomescarlosdev.codeflix.catalog.application.UseCase;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategorySearchQuery;
import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListResponse>> { }
