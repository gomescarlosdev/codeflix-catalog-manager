package br.com.gomescarlosdev.domain.validation.handler;

import br.com.gomescarlosdev.domain.exceptions.DomainException;
import br.com.gomescarlosdev.domain.validation.Error;
import br.com.gomescarlosdev.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public List<Error> getErrors() {
        return List.of();
    }

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
}
