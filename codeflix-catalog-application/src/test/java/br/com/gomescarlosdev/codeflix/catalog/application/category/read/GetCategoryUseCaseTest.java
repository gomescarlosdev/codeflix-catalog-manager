package br.com.gomescarlosdev.codeflix.catalog.application.category.read;

import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryGateway;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryID;
import br.com.gomescarlosdev.codeflix.catalog.domain.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCategoryUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    void givenAValidID_whenCallsFindById_thenReturnCategory() {
        var expectedName = "Adventure";
        var expectedDescription = "The most exciting category";
        var expectedActive = true;

        var category = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedActive
        );
        var expectedId = category.getId();

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category.clone()));

        var actualCategory = useCase.execute(expectedId.getValue());

        assertEquals(expectedId, actualCategory.id());
        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedActive, actualCategory.active());
        assertEquals(category.getCreatedAt(), actualCategory.createdAt());
        assertEquals(category.getUpdatedAt(), actualCategory.updatedAt());
        assertEquals(category.getDeletedAt(), actualCategory.deletedAt());

        verify(categoryGateway, times(1)).findById(expectedId);
    }

    @Test
    void givenAnInvalidID_whenCallsFindById_thenReturnCategoryNotFound() {
        final var expectedErrorMessage = "Category ID <123> was not found";
        final var expectedErrorCount = 1;
        var categoryId = CategoryID.from("123");

        when(categoryGateway.findById(categoryId)).thenReturn(Optional.empty());

        var actualException = assertThrows(
                DomainException.class,
                () -> useCase.execute(categoryId.getValue())
        );

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, times(1)).findById(categoryId);
    }

    @Test
    void givenAValidID_whenCallsFindByIdAndGatewayThrowsError_thenReturnException() {
        final var expectedErrorMessage = "Gateway error";
        var categoryId = CategoryID.from("123");

        when(categoryGateway.findById(categoryId)).thenThrow(new IllegalStateException("Gateway error"));

        var actualException = assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(categoryId.getValue())
        );

        assertEquals(expectedErrorMessage, actualException.getMessage());
        verify(categoryGateway, times(1)).findById(categoryId);
    }
}
