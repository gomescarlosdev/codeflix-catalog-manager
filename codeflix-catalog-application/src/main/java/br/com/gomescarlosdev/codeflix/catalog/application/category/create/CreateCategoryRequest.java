package br.com.gomescarlosdev.codeflix.catalog.application.category.create;

public record CreateCategoryRequest(String name, String description, boolean active) {
    public static CreateCategoryRequest with(
            String name,
            String description,
            boolean active
    ){
        return new CreateCategoryRequest(name, description, active);
    }
}
