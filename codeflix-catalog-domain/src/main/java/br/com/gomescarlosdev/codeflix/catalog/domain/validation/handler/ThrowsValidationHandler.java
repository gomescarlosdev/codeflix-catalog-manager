package br.com.gomescarlosdev.codeflix.catalog.domain.validation.handler;

import br.com.gomescarlosdev.codeflix.catalog.domain.exceptions.DomainException;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.Error;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final Error error) {
        throw DomainException.with(error);
    }

    @Override
    public ValidationHandler append(final ValidationHandler handler) {
        throw DomainException.with(handler.getErrors());
    }

    @Override
    public ValidationHandler validate(Validation validation) {
        try {
            validation.validate();
        } catch (final Exception ex){
            throw DomainException.with(List.of(new Error(ex.getMessage())));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
