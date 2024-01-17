package br.com.gomescarlosdev.codeflix.catalog.application.category.update;

import br.com.gomescarlosdev.codeflix.catalog.application.UseCase;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> { }
