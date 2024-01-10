package br.com.gomescarlosdev.codeflix.catalog.application.category.create;

import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenReturnCategoryID() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var command = CreateCategoryRequest.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.get().id());

        verify(categoryGateway, times(1))
                .create(argThat(category -> Objects.equals(expectedName, category.getName()) &&
                        Objects.equals(expectedDescription, category.getDescription()) &&
                        Objects.equals(expectedIsActive, category.isActive()) &&
                        Objects.nonNull(category.getId()) &&
                        Objects.nonNull(category.getCreatedAt()) &&
                        Objects.nonNull(category.getUpdatedAt()) &&
                        Objects.isNull(category.getDeletedAt())
                ));
    }

    @Test
    void givenAnInactiveCategory_whenCallsCreateCategory_thenReturnInactiveCategoryID() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = false;

        final var command = CreateCategoryRequest.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.get().id());

        verify(categoryGateway, times(1))
                .create(argThat(category -> Objects.equals(expectedName, category.getName()) &&
                        Objects.equals(expectedDescription, category.getDescription()) &&
                        Objects.equals(expectedIsActive, category.isActive()) &&
                        Objects.nonNull(category.getId()) &&
                        Objects.nonNull(category.getCreatedAt()) &&
                        Objects.nonNull(category.getUpdatedAt()) &&
                        Objects.nonNull(category.getDeletedAt())
                ));
    }

    @Test
    void givenAValidCommand_whenGatewayReturnsUnexpectedException_thenReturnException() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        when(categoryGateway.create(any())).thenThrow(new IllegalStateException("Gateway error"));

        final var command = CreateCategoryRequest.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var notification = useCase.execute(command).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());
        verify(categoryGateway, times(1))
                .create(argThat(category -> Objects.equals(expectedName, category.getName()) &&
                        Objects.equals(expectedDescription, category.getDescription()) &&
                        Objects.equals(expectedIsActive, category.isActive()) &&
                        Objects.nonNull(category.getId()) &&
                        Objects.nonNull(category.getCreatedAt()) &&
                        Objects.nonNull(category.getUpdatedAt()) &&
                        Objects.isNull(category.getDeletedAt())
                ));
    }

    @Test
    void givenAInvalidName_whenCallsCreateCategory_thenReturnException() {
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "category 'name' should not be 'null'";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryRequest.with(
                null,
                expectedDescription,
                expectedIsActive
        );

        final var notification = useCase.execute(command).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().message());
        verify(categoryGateway, times(0)).create(any());
    }

}
