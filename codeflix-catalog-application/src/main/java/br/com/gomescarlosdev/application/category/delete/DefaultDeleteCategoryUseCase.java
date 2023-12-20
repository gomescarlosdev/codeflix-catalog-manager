package br.com.gomescarlosdev.application.category.delete;

import br.com.gomescarlosdev.domain.category.CategoryGateway;
import br.com.gomescarlosdev.domain.category.CategoryID;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public void execute(String categoryId) {
        this.categoryGateway.deleteById(CategoryID.from(categoryId));
    }
}
