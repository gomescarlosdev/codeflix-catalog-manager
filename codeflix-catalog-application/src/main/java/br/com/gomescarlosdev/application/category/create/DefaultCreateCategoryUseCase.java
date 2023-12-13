package br.com.gomescarlosdev.application.category.create;

import br.com.gomescarlosdev.domain.category.Category;
import br.com.gomescarlosdev.domain.category.CategoryGateway;
import br.com.gomescarlosdev.domain.validation.handler.Notification;
import io.vavr.control.Either;

import static io.vavr.API.Left;
import static io.vavr.API.Try;


public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, CreateCategoryResponse> execute(CreateCategoryRequest command) {
        final var notification = Notification.create();

        final var category = Category.newCategory(
                command.name(),
                command.description(),
                command.active()
        );

        category.validate(Notification.create());

        return notification.hasError() ?
                Left(notification) :
                create(category);
    }

    private Either<Notification, CreateCategoryResponse> create(Category category) {
        return Try(() -> this.categoryGateway.create(category))
                .toEither()
                .bimap(Notification::create, CreateCategoryResponse::from);
    }

}
