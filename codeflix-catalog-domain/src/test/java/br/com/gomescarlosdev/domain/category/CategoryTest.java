package br.com.gomescarlosdev.domain.category;


import br.com.gomescarlosdev.domain.exceptions.DomainException;
import br.com.gomescarlosdev.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryTest {

    @Test
    void givenValidParams_whenCallNewCategory_thenInstantiateANewCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReturnError() {
        final var expectedDescription = "Most watched category";
        final var expectedErrorMessage = "category 'name' should not be 'null'";
        final var expectedErrorCount = 1;
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                null,
                expectedDescription,
                expectedIsActive
        );

        final var actualException = assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReturnError() {
        final var expectedName = "";
        final var expectedDescription = "Most watched category";
        final var expectedErrorMessage = "category 'name' should not be 'empty'";
        final var expectedErrorCount = 1;
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualException = assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidBlankName_whenCallNewCategoryAndValidate_thenShouldReturnError() {
        final var expectedName = "    ";
        final var expectedDescription = "Most watched category";
        final var expectedErrorMessage = "category 'name' should not be 'blank'";
        final var expectedErrorCount = 1;
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualException = assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReturnError() {
        final var expectedName = "Yu ";
        final var expectedDescription = "Most watched category";
        final var expectedErrorMessage = "category 'name' should be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualException = assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReturnError() {
        final var expectedName = """
                O Gerador de Lero-lero para TI e informática foi baseado no Fabuloso Gerador de Lero-lero v2.0."
                Ele é capaz de gerar qualquer quantidade de texto vazio e prolixo, ideal para engrossar uma tese de mestrado,
                impressionar seu chefe ou preparar discursos capazes de curar a insônia da platéia.
        """;
        final var expectedDescription = "Most watched category";
        final var expectedErrorMessage = "category 'name' should be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualException = assertThrows(
                DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler())
        );
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenAValidEmptyDescription_whenCallNewCategory_thenInstantiateANewCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "   ";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryDeactivated(){
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";

        final var category = Category.newCategory(
                expectedName,
                expectedDescription,
                true
        );

        await().atMost(200, TimeUnit.MILLISECONDS).untilAsserted(() ->
                assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()))
        );

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        assertTrue(category.isActive());
        assertNull(category.getDeletedAt());

        final var actualCategory = category.deactivate();

        assertDoesNotThrow(
                () -> category.validate(new ThrowsValidationHandler())
        );

        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(category.getName(), actualCategory.getName());
        assertEquals(category.getDescription(), actualCategory.getDescription());
        assertEquals(createdAt, actualCategory.getCreatedAt());
        assertFalse(actualCategory.isActive());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated(){
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";

        final var category = Category.newCategory(
                expectedName,
                expectedDescription,
                false
        );

        await().atMost(200, TimeUnit.MILLISECONDS).untilAsserted(() ->
                assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()))
        );

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        assertFalse(category.isActive());
        assertNotNull(category.getDeletedAt());

        final var actualCategory = category.activate();

        assertDoesNotThrow(
                () -> category.validate(new ThrowsValidationHandler())
        );

        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(category.getName(), actualCategory.getName());
        assertEquals(category.getDescription(), actualCategory.getDescription());
        assertEquals(createdAt, actualCategory.getCreatedAt());
        assertTrue(actualCategory.isActive());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidActiveCategory_whenCallUpdate_thenUpdateCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedActive = true;

        final var category = Category.newCategory(
                "Old Movies",
                "Category",
                false
        );

        await().atMost(200, TimeUnit.MILLISECONDS).untilAsserted(() ->
                assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()))
        );

        final var createdAt = category.getCreatedAt();
        final var updatedAt = category.getUpdatedAt();

        assertFalse(category.isActive());
        assertNotNull(category.getDeletedAt());

        final var actualCategory = category.update(
                expectedName,
                expectedDescription,
                expectedActive
        );

        assertDoesNotThrow(
                () -> category.validate(new ThrowsValidationHandler())
        );

        assertEquals(category.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(createdAt, actualCategory.getCreatedAt());
        assertEquals(category.isActive(), actualCategory.isActive());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNull(actualCategory.getDeletedAt());
    }


}
