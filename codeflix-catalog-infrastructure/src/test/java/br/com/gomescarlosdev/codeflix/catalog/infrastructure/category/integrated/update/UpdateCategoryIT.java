package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.integrated.update;

import br.com.gomescarlosdev.codeflix.catalog.application.category.update.UpdateCategoryCommand;
import br.com.gomescarlosdev.codeflix.catalog.application.category.update.UpdateCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryEntity;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.helper.SpringBootTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTestHelper
class UpdateCategoryIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_thenReturnCategoryID() {
        final var category = Category.newCategory(
                "Film",
                "Less watched category",
                false
        );
        save(category);
        assertEquals(1, categoryRepository.count());

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var expectedId = category.getId();
        final var updatedCategory = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualOutput = useCase.execute(updatedCategory);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.get().id());

        final var actualResult = categoryRepository.findById(actualOutput.get().id()).get();

        assertEquals(1, categoryRepository.count());
        assertEquals(actualOutput.get().id(), actualResult.getId());
        assertEquals(expectedName, actualResult.getName());
        assertEquals(expectedDescription, actualResult.getDescription());
        assertEquals(expectedIsActive, actualResult.isActive());
        assertEquals(
                category.getCreatedAt().truncatedTo(ChronoUnit.SECONDS),
                actualResult.getCreatedAt().truncatedTo(ChronoUnit.SECONDS)
        );
        assertTrue(category.getUpdatedAt().isBefore(actualResult.getUpdatedAt()));
        assertNull(actualResult.getDeletedAt());
    }

    private void save(final Category... category){
        categoryRepository.saveAllAndFlush(
                Arrays.stream(category).map(CategoryEntity::fromAggregate).toList()
        );
    }

}
