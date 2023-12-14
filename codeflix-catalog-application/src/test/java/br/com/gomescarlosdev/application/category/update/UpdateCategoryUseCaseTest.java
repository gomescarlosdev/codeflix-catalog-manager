package br.com.gomescarlosdev.application.category.update;

import br.com.gomescarlosdev.domain.category.Category;
import br.com.gomescarlosdev.domain.category.CategoryGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        final var updatedAt = category.getUpdatedAt();

        await().atMost(200, TimeUnit.MILLISECONDS).untilAsserted(() ->
                assertNotNull(category.getDeletedAt())
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

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(category));
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
                        updatedAt.isBefore(expectedCategory.getUpdatedAt()) &&
                        Objects.isNull(expectedCategory.getDeletedAt())
                )
        );
    }

//    @Test
//    void givenAnInactiveCategory_whenCallsCreateCategory_thenReturnInactiveCategoryID() {
//        final var expectedName = "Movies";
//        final var expectedDescription = "Most watched category";
//        final var expectedIsActive = false;
//
//        final var command = CreateCategoryRequest.with(
//                expectedName,
//                expectedDescription,
//                expectedIsActive
//        );
//
//        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());
//
//        final var actualOutput = useCase.execute(command);
//
//        assertNotNull(actualOutput);
//        assertNotNull(actualOutput.get().id());
//
//        verify(categoryGateway, times(1))
//                .create(argThat(category -> Objects.equals(expectedName, category.getName()) &&
//                        Objects.equals(expectedDescription, category.getDescription()) &&
//                        Objects.equals(expectedIsActive, category.isActive()) &&
//                        Objects.nonNull(category.getId()) &&
//                        Objects.nonNull(category.getCreatedAt()) &&
//                        Objects.nonNull(category.getUpdatedAt()) &&
//                        Objects.nonNull(category.getDeletedAt())
//                ));
//    }
//
//    @Test
//    void givenAValidCommand_whenGatewayReturnsUnexpectedException_thenReturnException() {
//        final var expectedName = "Movies";
//        final var expectedDescription = "Most watched category";
//        final var expectedIsActive = true;
//        final var expectedErrorMessage = "Gateway error";
//        final var expectedErrorCount = 1;
//
//        when(categoryGateway.create(any())).thenThrow(new IllegalStateException("Gateway error"));
//
//        final var command = CreateCategoryRequest.with(
//                expectedName,
//                expectedDescription,
//                expectedIsActive
//        );
//
//        final var notification = useCase.execute(command).getLeft();
//
//        assertEquals(expectedErrorCount, notification.getErrors().size());
//        assertEquals(expectedErrorMessage, notification.firstError().message());
//        verify(categoryGateway, times(1))
//                .create(argThat(category -> Objects.equals(expectedName, category.getName()) &&
//                        Objects.equals(expectedDescription, category.getDescription()) &&
//                        Objects.equals(expectedIsActive, category.isActive()) &&
//                        Objects.nonNull(category.getId()) &&
//                        Objects.nonNull(category.getCreatedAt()) &&
//                        Objects.nonNull(category.getUpdatedAt()) &&
//                        Objects.isNull(category.getDeletedAt())
//                ));
//    }
//
//    @Test
//    void givenAInvalidName_whenCallsCreateCategory_thenReturnException() {
//        final var expectedDescription = "Most watched category";
//        final var expectedIsActive = true;
//        final var expectedErrorMessage = "category 'name' should not be 'null'";
//        final var expectedErrorCount = 1;
//
//        final var command = CreateCategoryRequest.with(
//                null,
//                expectedDescription,
//                expectedIsActive
//        );
//
//        final var notification = useCase.execute(command).getLeft();
//
//        assertEquals(expectedErrorCount, notification.getErrors().size());
//        assertEquals(expectedErrorMessage, notification.firstError().message());
//        verify(categoryGateway, times(0)).create(any());
//    }

}
