package br.com.gomescarlosdev.codeflix.catalog.application.category.create;

public record CreateCategoryCommand(String name, String description, boolean active) {
    public static CreateCategoryCommand with(
            String name,
            String description,
            boolean active
    ){
        return new CreateCategoryCommand(name, description, active);
    }
}
