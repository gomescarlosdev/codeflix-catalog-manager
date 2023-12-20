package br.com.gomescarlosdev.application.category.read;

import br.com.gomescarlosdev.domain.category.CategoryGateway;
import br.com.gomescarlosdev.domain.category.CategoryID;
import br.com.gomescarlosdev.domain.exceptions.DomainException;
import br.com.gomescarlosdev.domain.validation.Error;

import java.util.function.Supplier;

public class DefaultGetCategoryUseCase extends GetCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public GetCategoryResponse execute(String categoryId) {
        return this.categoryGateway.findById(CategoryID.from(categoryId))
                .map(GetCategoryResponse::from)
                .orElseThrow(notFound(categoryId));
    }

    private static Supplier<DomainException> notFound(String categoryId) {
        return () -> DomainException.with(
                new Error("Category ID <%s> was not found".formatted(categoryId))
        );
    }
}
