package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category;

import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryGateway;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryID;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategorySearchQuery;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryEntity;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.helper.TestConfigHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfigHelper
class CategoryGatewayImplTest {

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidCategory_whenCallsCreate_thenReturnNewCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertEquals(0, categoryRepository.count());

        var actualCategory = categoryGateway.create(aCategory);

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        assertEquals(1, categoryRepository.count());
        var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        assertEquals(aCategory.getName(), actualEntity.getName());
        assertEquals(aCategory.getDescription(), actualEntity.getDescription());
        assertEquals(aCategory.isActive(), actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenCallsUpdate_thenReturnCategoryUpdated() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(
                "Film",
                "Long duration",
                false
        );

        categoryRepository.saveAndFlush(CategoryEntity.fromAggregate(aCategory));
        assertEquals(1, categoryRepository.count());

        var aUpdatedCategory = aCategory.clone().update(expectedName, expectedDescription, expectedIsActive);
        var actualCategory = categoryGateway.update(aUpdatedCategory);

        assertEquals(1, categoryRepository.count());
        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertNotEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertNotEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        assertEquals(1, categoryRepository.count());
        var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        assertNotEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAValidCategoryID_whenCallsDelete_thenReturnSuccess() {
        final var aCategory = Category.newCategory(
                "Film",
                "Long duration",
                false
        );
        categoryRepository.saveAndFlush(CategoryEntity.fromAggregate(aCategory));
        assertEquals(1, categoryRepository.count());

        categoryGateway.deleteById(aCategory.getId());

        assertEquals(0, categoryRepository.count());
    }

    @Test
    void givenAnInvalidCategoryID_whenCallsDelete_thenReturnSuccess() {
        final var aCategory = Category.newCategory(
                "Film",
                "Long duration",
                false
        );
        categoryRepository.saveAndFlush(CategoryEntity.fromAggregate(aCategory));
        assertEquals(1, categoryRepository.count());

        categoryGateway.deleteById(CategoryID.from("123"));

        assertEquals(1, categoryRepository.count());
    }

    @Test
    void givenAValidCategoryID_whenCallsFindById_thenReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        categoryRepository.saveAndFlush(CategoryEntity.fromAggregate(aCategory));
        assertEquals(1, categoryRepository.count());

        var actualCategory = categoryGateway.findById(aCategory.getId()).get();

        assertEquals(1, categoryRepository.count());
        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidCategoryID_whenCallsFindById_thenReturnEmpty() {
        var actualCategory = categoryGateway.findById(CategoryID.from("23424"));
        assertEquals(0, categoryRepository.count());
        assertTrue(actualCategory.isEmpty());
    }

    @Test
    void givenSomePreStoredRegisters_whenCallsFindAll_thenReturnAPaginatedListOfCategories() {
        final var expectedPage = 0;
        final var expectedOffset = 1;
        final var expectedTotal = 3;

        Category movies = Category.newCategory("Movies", "All Movies", true);
        Category series = Category.newCategory("Series", "All Series", true);
        Category documentaries = Category.newCategory("Documentaries", "All Documentaries", true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryEntity.fromAggregate(movies),
                CategoryEntity.fromAggregate(series),
                CategoryEntity.fromAggregate(documentaries)
        ));

        assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(
                0, 1, "", "name" , "asc"
        );

        var actualResult = categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(documentaries.getId().getValue(), actualResult.items().get(0).getId().getValue());
    }

    @Test
    void givenNoPreStoredRegisters_whenCallsFindAll_thenReturnAnEmptyPaginatedListOfCategories() {
        final var expectedPage = 0;
        final var expectedOffset = 1;
        final var expectedTotal = 0;

        assertEquals(0, categoryRepository.count());

        var query = new CategorySearchQuery(
                0, 1, "", "name" , "asc"
        );
        var actualResult = categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(expectedTotal, actualResult.total());
    }

    @Test
    void givenFollowPagination_whenCallsFindAll_thenReturnAPaginatedListOfCategories() {
        var expectedPage = 0;
        final var expectedOffset = 1;
        final var expectedTotal = 3;

        Category movies = Category.newCategory("Movies", "All Movies", true);
        Category series = Category.newCategory("Series", "All Series", true);
        Category documentaries = Category.newCategory("Documentaries", "All Documentaries", true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryEntity.fromAggregate(movies),
                CategoryEntity.fromAggregate(series),
                CategoryEntity.fromAggregate(documentaries)
        ));

        assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(
                0, 1, "", "name" , "asc"
        );
        var actualResult = categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(documentaries.getId().getValue(), actualResult.items().get(0).getId().getValue());

        expectedPage = 1;
        query = new CategorySearchQuery(
                1, 1, "", "name" , "asc"
        );
        actualResult = categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(movies.getId().getValue(), actualResult.items().get(0).getId().getValue());

        expectedPage = 2;
        query = new CategorySearchQuery(
                2, 1, "", "name" , "asc"
        );
        actualResult = categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(series.getId().getValue(), actualResult.items().get(0).getId().getValue());
    }

    @Test
    void givenAQueryWithATermForCategoryName_whenCallsFindAllAndTermMatches_thenReturnAPaginatedListOfCategories() {
        final var expectedPage = 0;
        final var expectedOffset = 1;
        final var expectedTotal = 1;

        Category movies = Category.newCategory("Movies", "All Movies", true);
        Category series = Category.newCategory("Series", "All Series", true);
        Category documentaries = Category.newCategory("Documentaries", "All Documentaries", true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryEntity.fromAggregate(movies),
                CategoryEntity.fromAggregate(series),
                CategoryEntity.fromAggregate(documentaries)
        ));

        assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(
                0, 1, "doc", "name" , "asc"
        );

        var actualResult = categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(documentaries.getId().getValue(), actualResult.items().get(0).getId().getValue());
        assertEquals(documentaries.getName(), actualResult.items().get(0).getName());
    }

    @Test
    void givenAQueryWithATermForCategoryDescription_whenCallsFindAllAndTermMatches_thenReturnAPaginatedListOfCategories() {
        final var expectedPage = 0;
        final var expectedOffset = 1;
        final var expectedTotal = 1;

        Category movies = Category.newCategory("Movies", "The Greatest Movies Ever", true);
        Category series = Category.newCategory("Series", "All Series", true);
        Category documentaries = Category.newCategory("Documentaries", "All Documentaries", true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryEntity.fromAggregate(movies),
                CategoryEntity.fromAggregate(series),
                CategoryEntity.fromAggregate(documentaries)
        ));

        assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(
                0, 1, "greatest", "name" , "asc"
        );

        var actualResult = categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(movies.getId().getValue(), actualResult.items().get(0).getId().getValue());
        assertEquals(movies.getName(), actualResult.items().get(0).getName());
    }

}
