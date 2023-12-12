package br.com.gomescarlosdev.application.category.create;

import br.com.gomescarlosdev.domain.category.CategoryGateway;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateCategoryUseCaseTest {

    @Test
    void givenAValidCommand_whenCallsCreateCategory_thenReturnCategoryID() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var categoryGateway = mock(CategoryGateway.class);
        final var useCase = new DefaultCreateCategoryUseCase(categoryGateway);

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(command);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

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

}
