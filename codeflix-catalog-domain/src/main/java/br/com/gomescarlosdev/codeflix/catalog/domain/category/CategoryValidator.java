package br.com.gomescarlosdev.codeflix.catalog.domain.category;

import br.com.gomescarlosdev.codeflix.catalog.domain.validation.Error;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.ValidationHandler;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.Validator;

public class CategoryValidator extends Validator {

    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 255;
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
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        if (category.getName() == null){
            this.validationHandler().append(new Error("category 'name' should not be 'null'"));
            return;
        }
        if (category.getName().isEmpty()){
            this.validationHandler().append(new Error("category 'name' should not be 'empty'"));
            return;
        }
        if (category.getName().isBlank()){
            this.validationHandler().append(new Error("category 'name' should not be 'blank'"));
            return;
        }
        final var len = category.getName().trim().length();
        if (len < NAME_MIN_LENGTH || len > NAME_MAX_LENGTH){
            this.validationHandler().append(new Error("category 'name' should be between 3 and 255 characters"));
        }
    }
}
