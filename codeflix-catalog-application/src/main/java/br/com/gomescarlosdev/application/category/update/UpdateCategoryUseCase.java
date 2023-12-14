package br.com.gomescarlosdev.application.category.update;

import br.com.gomescarlosdev.application.UseCase;
import br.com.gomescarlosdev.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryRequest, Either<Notification, UpdateCategoryResponse>> { }
