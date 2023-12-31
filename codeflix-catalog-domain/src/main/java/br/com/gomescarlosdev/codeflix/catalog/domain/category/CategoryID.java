package br.com.gomescarlosdev.codeflix.catalog.domain.category;

import br.com.gomescarlosdev.codeflix.catalog.domain.Identifier;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = false)
public class CategoryID extends Identifier {

    private final String value;

    private CategoryID(@NonNull final String value){
        this.value = value;
    }

    public static CategoryID unique(){
        return from(UUID.randomUUID());
    }

    public static CategoryID from(final String id){
        return new CategoryID(id);
    }

    public static CategoryID from(final UUID uuid){
        return new CategoryID(uuid.toString().toLowerCase());
    }

}
