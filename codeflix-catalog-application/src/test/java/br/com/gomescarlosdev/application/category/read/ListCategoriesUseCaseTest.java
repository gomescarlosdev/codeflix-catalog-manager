package br.com.gomescarlosdev.application.category.read;

import br.com.gomescarlosdev.codeflix.catalog.application.category.read.CategoryListResponse;
import br.com.gomescarlosdev.codeflix.catalog.application.category.read.DefaultListCategoriesUseCase;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryGateway;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategorySearchQuery;
import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    void givenAValidQuery_whenCallsFindAll_thenReturnAListOfCategories() {
        final var categories = List.of(
                Category.newCategory("Movies", "Most watched category", true),
                Category.newCategory("Adventure", "Most exciting category", true)
        );

        final var expectedPage = 0;
        final var expectedOffset = 10;
        final var expectedTerms = "";
        final var expectedOrderBy = "createdAt";
        final var expectedDirection = "desc";

        final var query = new CategorySearchQuery(
                expectedPage, expectedOffset, expectedTerms, expectedOrderBy, expectedDirection
        );

        final var expectedPagination = new Pagination<>(
                expectedPage, expectedOffset, categories.size(), categories
        );

        final var expectedTotalOfItems = expectedPagination.items().size();
        final var expectedResult = expectedPagination.map(CategoryListResponse::from);


        when(categoryGateway.findAll(query)).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        assertEquals(expectedTotalOfItems, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(categories.size(), actualResult.total());
    }

    @Test
    void givenAValidQuery_whenCallsFindAllAndHasNoResults_thenReturnAnEmptyList() {
        final var categories = List.<Category>of();

        final var expectedPage = 0;
        final var expectedOffset = 10;
        final var expectedTerms = "";
        final var expectedOrderBy = "createdAt";
        final var expectedDirection = "desc";

        final var query = new CategorySearchQuery(
                expectedPage, expectedOffset, expectedTerms, expectedOrderBy, expectedDirection
        );

        final var expectedPagination = new Pagination<>(
                expectedPage, expectedOffset, 0, categories
        );

        final var expectedTotalOfItems = expectedPagination.items().size();
        final var expectedResult = expectedPagination.map(CategoryListResponse::from);


        when(categoryGateway.findAll(query)).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(query);

        assertEquals(expectedTotalOfItems, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(0, actualResult.total());
    }

    @Test
    void givenAValidQuery_whenCallsFindAllAndGatewayThrowsError_thenReturnException() {
        final var expectedPage = 0;
        final var expectedOffset = 10;
        final var expectedTerms = "";
        final var expectedOrderBy = "createdAt";
        final var expectedDirection = "desc";
        final var expectedErrorMessage = "Gateway error";

        final var query = new CategorySearchQuery(
                expectedPage, expectedOffset, expectedTerms, expectedOrderBy, expectedDirection
        );


        when(categoryGateway.findAll(query)).thenThrow(new IllegalStateException("Gateway error"));

        var actualException = assertThrows(
                IllegalStateException.class, () -> useCase.execute(query)
        );

        assertEquals(expectedErrorMessage, actualException.getMessage());

    }
}
