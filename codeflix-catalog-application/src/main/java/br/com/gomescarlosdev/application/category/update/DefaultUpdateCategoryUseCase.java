package br.com.gomescarlosdev.application.category.update;

import br.com.gomescarlosdev.domain.category.Category;
import br.com.gomescarlosdev.domain.category.CategoryGateway;
import br.com.gomescarlosdev.domain.category.CategoryID;
import br.com.gomescarlosdev.domain.validation.handler.Notification;
import io.vavr.control.Either;

import static io.vavr.API.Try;
import static io.vavr.control.Either.left;


public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, UpdateCategoryResponse> execute(UpdateCategoryRequest updateCategoryRequest) {

        var optCategory = categoryGateway.findById(CategoryID.from(updateCategoryRequest.id()));

        if (optCategory.isPresent()) {
            var category = optCategory.get();
            category.update(
                    updateCategoryRequest.name(),
                    updateCategoryRequest.description(),
                    updateCategoryRequest.active()
            );
            final var notification = Notification.create();
            category.validate(notification);
            return notification.hasError() ? left(notification) : update(category);
        }
        return left(Notification.create(new IllegalStateException("category not found")));
    }

    private Either<Notification, UpdateCategoryResponse> update(Category category) {
        return Try(() -> this.categoryGateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryResponse::from);
    }

}
