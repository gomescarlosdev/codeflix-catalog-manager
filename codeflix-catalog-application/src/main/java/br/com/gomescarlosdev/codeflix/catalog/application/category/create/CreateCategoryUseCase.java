package br.com.gomescarlosdev.codeflix.catalog.application.category.create;

import br.com.gomescarlosdev.codeflix.catalog.application.UseCase;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryRequest, Either<Notification, CreateCategoryResponse>> { }
