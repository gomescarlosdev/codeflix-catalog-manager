package br.com.gomescarlosdev.codeflix.catalog.application.category.update;

public record UpdateCategoryRequest(String id, String name, String description, boolean active) {
    public static UpdateCategoryRequest with(
            String id,
            String name,
            String description,
            boolean active
    ) {
       return new UpdateCategoryRequest(id, name, description, active);
    }
}
