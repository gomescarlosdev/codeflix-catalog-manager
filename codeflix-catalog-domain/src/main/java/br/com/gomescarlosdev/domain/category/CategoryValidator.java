package br.com.gomescarlosdev.domain.category;

import br.com.gomescarlosdev.domain.validation.Error;
import br.com.gomescarlosdev.domain.validation.ValidationHandler;
import br.com.gomescarlosdev.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private final Category category;

    protected CategoryValidator(
            final Category category,
            final ValidationHandler handler
    ) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        if (category.getName() == null){
            this.validationHandler().append(new Error("category 'name' should not be 'null'"));
        }
    }
}
