package br.com.gomescarlosdev.codeflix.catalog.application.category.create;

import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryGateway;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

import static io.vavr.API.Try;
import static io.vavr.control.Either.left;


public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, CreateCategoryResponse> execute(CreateCategoryRequest command) {
        final var category = Category.newCategory(
                command.name(),
                command.description(),
                command.active()
        );
        final var notification = Notification.create();
        category.validate(notification);

        return notification.hasError() ? left(notification) : create(category);
    }

    private Either<Notification, CreateCategoryResponse> create(Category category) {
        return Try(() -> this.categoryGateway.create(category))
                .toEither()
                .bimap(Notification::create, CreateCategoryResponse::from);
    }

}
