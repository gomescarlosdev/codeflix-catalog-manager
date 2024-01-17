package br.com.gomescarlosdev.codeflix.catalog.infrastructure.api.controllers;

import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryCommand;
import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryOutput;
import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.read.GetCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.update.UpdateCategoryCommand;
import br.com.gomescarlosdev.codeflix.catalog.application.category.update.UpdateCategoryOutput;
import br.com.gomescarlosdev.codeflix.catalog.application.category.update.UpdateCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.Pagination;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.handler.Notification;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.api.CategoryApi;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.models.request.CreateCategoryRequest;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.models.request.UpdateCategoryRequest;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.models.response.CategoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryApi {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryUseCase getCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryUseCase getCategoryUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase
    ) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoryUseCase = getCategoryUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
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
    public ResponseEntity<?> updateById(String id, UpdateCategoryRequest request) {
        var command = UpdateCategoryCommand.with(
                id,
                request.name(),
                request.description(),
                request.active()
        );

        final Function<Notification, ResponseEntity<?>> onError =
                notification -> ResponseEntity.unprocessableEntity().body(notification);
        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return updateCategoryUseCase.execute(command).fold(onError, onSuccess);
    }

    @Override
    public ResponseEntity<?> deleteById(String id) {
        return null;
    }

    @Override
    public CategoryResponse getCategoryById(String id) {
        return CategoryResponse.from(getCategoryUseCase.execute(id));
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
