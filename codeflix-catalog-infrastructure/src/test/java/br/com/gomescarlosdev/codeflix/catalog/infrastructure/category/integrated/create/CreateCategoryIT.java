package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.integrated.create;

import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryCommand;
import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.helper.SpringBootTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTestHelper
class CreateCategoryIT {

    @Autowired
    private CreateCategoryUseCase useCase;
    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenReturnCategoryID() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var category = CreateCategoryCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertEquals(0, categoryRepository.count());

        final var actualOutput = useCase.execute(category);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.get().id());

        final var actualResult = categoryRepository.findById(actualOutput.get().id()).get();

        assertEquals(1, categoryRepository.count());
        assertEquals(expectedName, actualResult.getName());
        assertEquals(expectedDescription, actualResult.getDescription());
        assertEquals(expectedIsActive, actualResult.isActive());
        assertNotNull(actualResult.getCreatedAt());
        assertNotNull(actualResult.getUpdatedAt());
        assertNull(actualResult.getDeletedAt());
    }

}
