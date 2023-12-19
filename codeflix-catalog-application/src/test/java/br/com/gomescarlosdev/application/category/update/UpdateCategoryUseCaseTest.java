package br.com.gomescarlosdev.application.category.update;

import br.com.gomescarlosdev.domain.category.Category;
import br.com.gomescarlosdev.domain.category.CategoryGateway;
import br.com.gomescarlosdev.domain.category.CategoryID;
import br.com.gomescarlosdev.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_thenReturnCategoryID() {
        final var category = Category.newCategory(
                "Film",
                "Less watched category",
                false
        );

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var expectedId = category.getId();
        final var command = UpdateCategoryRequest.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.get().id());

        verify(categoryGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).update(
                argThat(expectedCategory -> Objects.equals(expectedName, expectedCategory.getName()) &&
                        Objects.equals(expectedDescription, expectedCategory.getDescription()) &&
                        Objects.equals(expectedIsActive, expectedCategory.isActive()) &&
                        Objects.equals(expectedId, expectedCategory.getId()) &&
                        Objects.nonNull(expectedCategory.getCreatedAt()) &&
                        Objects.nonNull(expectedCategory.getUpdatedAt()) &&
                        Objects.isNull(expectedCategory.getDeletedAt())
                )
        );
    }

    @Test
    void givenAValidInactivateCommand_whenCallsUpdateCategory_thenReturnInactivatedCategoryID() {
        final var category = Category.newCategory(
                "Movies",
                "Most watched category",
                true
        );

        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = false;

        final var expectedId = category.getId();
        final var command = UpdateCategoryRequest.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category.clone()));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        assertTrue(category.isActive());
        assertNull(category.getDeletedAt());

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.get().id());

        verify(categoryGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).update(
                argThat(expectedCategory ->
                                Objects.equals(expectedName, expectedCategory.getName()) &&
                                Objects.equals(expectedDescription, expectedCategory.getDescription()) &&
                                Objects.equals(expectedIsActive, expectedCategory.isActive()) &&
                                Objects.equals(expectedId, expectedCategory.getId()) &&
                                Objects.nonNull(expectedCategory.getCreatedAt()) &&
                                Objects.nonNull(expectedCategory.getUpdatedAt()) &&
                                !Objects.equals(category.isActive(), expectedCategory.isActive()) &&
                                Objects.nonNull(expectedCategory.getDeletedAt())
                )
        );
    }

    @Test
    void givenAnInvalidCommand_whenCallsUpdateCategory_thenReturnDomainException() {
        final var category = Category.newCategory(
                "Film",
                "Less watched category",
                false
        );

        final var expectedId = category.getId();
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "category 'name' should not be 'null'";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryRequest.with(
                expectedId.getValue(),
                null,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category.clone()));

        final var notification = useCase.execute(command).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(0)).update(any());
    }

    @Test
    void givenAnInvalidId_whenCallsUpdateCategory_thenReturnNotFoundException() {

        final var expectedId = "123";
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Category ID <123> was not found";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryRequest.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(CategoryID.from(expectedId))).thenReturn(Optional.empty());

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, times(1)).findById(CategoryID.from(expectedId));
        verify(categoryGateway, times(0)).update(any());
    }

}
