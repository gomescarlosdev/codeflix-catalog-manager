package br.com.gomescarlosdev.codeflix.catalog.application.category.update;

public record UpdateCategoryCommand(String id, String name, String description, boolean active) {
    public static UpdateCategoryCommand with(
            String id,
            String name,
            String description,
            boolean active
    ) {
       return new UpdateCategoryCommand(id, name, description, active);
    }
}
