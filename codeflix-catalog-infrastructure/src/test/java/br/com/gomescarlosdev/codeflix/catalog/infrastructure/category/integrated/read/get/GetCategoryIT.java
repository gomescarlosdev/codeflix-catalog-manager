package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.integrated.read.get;

import br.com.gomescarlosdev.codeflix.catalog.application.category.read.GetCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryEntity;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.helper.SpringBootTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTestHelper
class GetCategoryIT {

    @Autowired
    private GetCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidID_whenCallsFindById_thenReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedActive = true;

        final var aCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedActive
        );

        categoryRepository.saveAndFlush(CategoryEntity.fromAggregate(aCategory));
        assertEquals(1, categoryRepository.count());

        var expectedId = aCategory.getId().getValue();

        var actualCategory = useCase.execute(expectedId);

        assertEquals(expectedId, actualCategory.id().getValue());
        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedActive, actualCategory.active());
        assertNotNull(actualCategory.createdAt());
        assertNotNull(actualCategory.updatedAt());
        assertNull(actualCategory.deletedAt());
    }

}
