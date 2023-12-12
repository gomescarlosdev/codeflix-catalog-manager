package br.com.gomescarlosdev.application.category.create;

import br.com.gomescarlosdev.domain.category.Category;
import br.com.gomescarlosdev.domain.category.CategoryGateway;
import br.com.gomescarlosdev.domain.validation.handler.ThrowsValidationHandler;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(CategoryGateway categoryGateway){
        this.categoryGateway = categoryGateway;
    }

    @Override
    public CreateCategoryOutput execute(CreateCategoryCommand command) {
        final var name = command.name();
        final var description = command.description();
        final var active = command.active();

        final var category = Category.newCategory(name, description, active);
        category.validate(new ThrowsValidationHandler());

        return CreateCategoryOutput.from(categoryGateway.create(category));
    }

}
