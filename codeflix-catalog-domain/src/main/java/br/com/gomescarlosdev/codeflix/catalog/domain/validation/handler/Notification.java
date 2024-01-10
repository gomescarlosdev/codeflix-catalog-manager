package br.com.gomescarlosdev.codeflix.catalog.domain.validation.handler;

import br.com.gomescarlosdev.codeflix.catalog.domain.exceptions.DomainException;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.Error;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errorList;

    private Notification(List<Error> errorList) {
        this.errorList = errorList;
    }

    public static Notification create(){
        return new Notification(new ArrayList<>());
    }

    public static Notification create(Error error){
        return new Notification(new ArrayList<>()).append(error);
    }

    public static Notification create(Throwable throwable){
        return new Notification(new ArrayList<>())
                .append(new Error(throwable.getMessage()));
    }

    @Override
    public Notification append(Error error) {
        this.errorList.add(error);
        return this;
    }

    @Override
    public Notification append(ValidationHandler handler) {
        this.errorList.addAll(handler.getErrors());
        return this;
    }

    @Override
    public Notification validate(Validation validation) {
        try {
            validation.validate();
        } catch (DomainException de) {
            this.errorList.addAll(de.getErrors());
        } catch (Exception ex) {
            this.errorList.add(new Error(ex.getMessage()));
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errorList;
    }
}
