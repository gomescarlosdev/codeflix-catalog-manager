package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.integrated.delete;

import br.com.gomescarlosdev.codeflix.catalog.application.category.delete.DeleteCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryID;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryEntity;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.helper.SpringBootTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTestHelper
class DeleteCategoryIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidCategoryID_whenCallsDeleteCategory_thenReturnOK() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedActive = true;

        final var aCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedActive
        );

        save(aCategory);
        assertEquals(1, categoryRepository.count());
        assertDoesNotThrow(() -> useCase.execute(aCategory.getId().getValue()));
    }

    @Test
    void givenAnInvalidCategoryID_whenCallsDeleteCategory_thenReturnOK() {
        var categoryID = CategoryID.from("123").getValue();

        assertEquals(0, categoryRepository.count());
        assertDoesNotThrow(() -> useCase.execute(categoryID));
    }

    private void save(final Category... category){
        categoryRepository.saveAllAndFlush(
                Arrays.stream(category).map(CategoryEntity::fromAggregate).toList()
        );
    }
}
