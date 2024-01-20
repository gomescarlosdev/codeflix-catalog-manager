package br.com.gomescarlosdev.codeflix.catalog.application.category.read.get;

import br.com.gomescarlosdev.codeflix.catalog.application.category.read.CategoryOutput;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryGateway;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryID;
import br.com.gomescarlosdev.codeflix.catalog.domain.exceptions.DomainException;
import br.com.gomescarlosdev.codeflix.catalog.domain.exceptions.NotFoundException;

import java.util.function.Supplier;

public class DefaultGetCategoryUseCase extends GetCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public CategoryOutput execute(String categoryId) {
        final var aCategoryId = CategoryID.from(categoryId);
        return this.categoryGateway.findById(aCategoryId)
                .map(CategoryOutput::from).orElseThrow(notFound(aCategoryId));
    }

    private static Supplier<DomainException> notFound(CategoryID categoryId) {
        return () -> NotFoundException.with(Category.class, categoryId);
    }
}
