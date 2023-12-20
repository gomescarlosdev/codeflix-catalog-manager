package br.com.gomescarlosdev.application.category.delete;

import br.com.gomescarlosdev.domain.category.CategoryGateway;
import br.com.gomescarlosdev.domain.category.CategoryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    void givenAValidCategoryID_whenCallsDeleteCategory_thenReturnOK() {
        var categoryID = CategoryID.unique();

        doNothing().when(categoryGateway).deleteById(categoryID);

        assertDoesNotThrow(() -> useCase.execute(categoryID.getValue()));
        verify(categoryGateway, times(1)).deleteById(categoryID);
    }

    @Test
    void givenAnInvalidCategoryID_whenCallsDeleteCategory_thenReturnOK() {
        var categoryID = CategoryID.from("123");

        doNothing().when(categoryGateway).deleteById(categoryID);

        assertDoesNotThrow(() -> useCase.execute(categoryID.getValue()));
        verify(categoryGateway, times(1)).deleteById(categoryID);
    }

    @Test
    void givenAValidCategoryID_whenCallsDeleteCategoryAndGatewayThrowsError_thenReturnException() {
        var categoryID = CategoryID.unique();

        doThrow(new IllegalStateException("Gateway error")).when(categoryGateway).deleteById(categoryID);

        assertThrows(IllegalStateException.class, () -> useCase.execute(categoryID.getValue()));
        verify(categoryGateway, times(1)).deleteById(categoryID);
    }
}
