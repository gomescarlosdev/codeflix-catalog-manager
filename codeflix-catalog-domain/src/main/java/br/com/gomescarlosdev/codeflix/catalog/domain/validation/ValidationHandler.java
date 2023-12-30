package br.com.gomescarlosdev.codeflix.catalog.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error error);
    ValidationHandler append(ValidationHandler handler);
    ValidationHandler validate(Validation validation);

    List<Error> getErrors();

    default Error firstError(){
        if(getErrors() != null && !getErrors().isEmpty()){
            return getErrors().get(0);
        }
        return null;
    }

    default boolean hasError(){
        return getErrors() != null && !getErrors().isEmpty();
    }

    interface Validation {
        void validate();
    }

}
