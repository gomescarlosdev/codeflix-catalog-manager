package br.com.gomescarlosdev.application.category.update;

import br.com.gomescarlosdev.domain.category.Category;
import br.com.gomescarlosdev.domain.category.CategoryGateway;
import br.com.gomescarlosdev.domain.category.CategoryID;
import br.com.gomescarlosdev.domain.exceptions.DomainException;
import br.com.gomescarlosdev.domain.validation.Error;
import br.com.gomescarlosdev.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.function.Supplier;

import static io.vavr.API.Try;
import static io.vavr.control.Either.left;


public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, UpdateCategoryResponse> execute(UpdateCategoryRequest updateCategoryRequest) {

        var category = categoryGateway.findById(
                CategoryID.from(updateCategoryRequest.id())
        ).orElseThrow(notFound(updateCategoryRequest.id()));

        final var notification = Notification.create();
        category.update(
                updateCategoryRequest.name(),
                updateCategoryRequest.description(),
                updateCategoryRequest.active()
        ).validate(notification);

        return notification.hasError() ? left(notification) : update(category);
    }

    private Either<Notification, UpdateCategoryResponse> update(Category category) {
        return Try(() -> this.categoryGateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryResponse::from);
    }

    private static Supplier<DomainException> notFound(String categoryId) {
        return () -> DomainException.with(new Error("Category ID <%s> was not found"
                .formatted(categoryId))
        );
    }

}
