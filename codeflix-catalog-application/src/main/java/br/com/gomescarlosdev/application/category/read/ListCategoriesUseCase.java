package br.com.gomescarlosdev.application.category.read;

import br.com.gomescarlosdev.application.UseCase;
import br.com.gomescarlosdev.domain.category.CategorySearchQuery;
import br.com.gomescarlosdev.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListResponse>> { }
