package br.com.gomescarlosdev.application.category.create;

import br.com.gomescarlosdev.application.UseCase;
import br.com.gomescarlosdev.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryRequest, Either<Notification, CreateCategoryResponse>> { }
