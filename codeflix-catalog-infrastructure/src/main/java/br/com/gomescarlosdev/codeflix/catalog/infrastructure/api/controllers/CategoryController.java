package br.com.gomescarlosdev.codeflix.catalog.infrastructure.api.controllers;

import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryCommand;
import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryOutput;
import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.Pagination;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.handler.Notification;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.api.CategoryApi;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.models.CreateCategoryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryApi {

    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
    }

    @Override
    public ResponseEntity<?> create(CreateCategoryRequest request) {
        var command = CreateCategoryCommand.with(
                request.name(),
                request.description(),
                request.active()
        );

        final Function<Notification, ResponseEntity<?>> onError =
                notification -> ResponseEntity.unprocessableEntity().body(notification);
        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess =
                output -> ResponseEntity.created(URI.create("/v1/categories")).body(output);

        return createCategoryUseCase.execute(command).fold(onError, onSuccess);
    }

    @Override
    public ResponseEntity<?> update() {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteById(String categoryId) {
        return null;
    }

    @Override
    public ResponseEntity<String> getCategoryById(String categoryId) {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }

    @Override
    public Pagination<?> listAllCategories(
            String term,
            int page,
            int offset,
            String orderBy,
            String direction
    ) {
        return null;
    }
}
