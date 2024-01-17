package br.com.gomescarlosdev.codeflix.catalog.application.category.update;

import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryGateway;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryID;
import br.com.gomescarlosdev.codeflix.catalog.domain.exceptions.DomainException;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.Error;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.handler.Notification;
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
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand command) {

        var category = categoryGateway.findById(
                CategoryID.from(command.id())
        ).orElseThrow(notFound(command.id()));

        final var notification = Notification.create();
        category.update(
                command.name(),
                command.description(),
                command.active()
        ).validate(notification);

        return notification.hasError() ? left(notification) : update(category);
    }

    private Either<Notification, UpdateCategoryOutput> update(Category category) {
        return Try(() -> this.categoryGateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private static Supplier<DomainException> notFound(String categoryId) {
        return () -> DomainException.with(new Error("Category ID <%s> was not found"
                .formatted(categoryId))
        );
    }

}
