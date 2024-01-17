package br.com.gomescarlosdev.codeflix.catalog.domain.exceptions;

import br.com.gomescarlosdev.codeflix.catalog.domain.AggregateRoot;
import br.com.gomescarlosdev.codeflix.catalog.domain.Identifier;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.Error;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class NotFoundException extends DomainException {

    protected NotFoundException(final String message, final List<Error> errors){
        super(message, errors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> aggregate,
            final Identifier identifier
    ) {
        final var errorMessage = "%s with ID %s was not found".formatted(
                aggregate.getSimpleName(), identifier.getValue()
        );
        return new NotFoundException(errorMessage, Collections.emptyList());
    }

    public static NotFoundException with(final Error error) {
        return new NotFoundException(error.message(), List.of(error));
    }

}
