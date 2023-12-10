package br.com.gomescarlosdev.domain;

import br.com.gomescarlosdev.domain.validation.ValidationHandler;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode
public abstract class Entity<ID extends Identifier> {

    protected final ID id;

    protected Entity(@NonNull final ID id){
        this.id = id;
    }

    public abstract void validate(ValidationHandler handler);

}
