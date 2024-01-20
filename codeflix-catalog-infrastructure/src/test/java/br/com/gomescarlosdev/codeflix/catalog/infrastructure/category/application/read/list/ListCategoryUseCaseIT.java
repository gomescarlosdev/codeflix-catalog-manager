package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.application.read.list;

import br.com.gomescarlosdev.codeflix.catalog.application.category.read.list.ListCategoriesUseCase;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.SearchQuery;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryJpaRepository;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.helper.SpringBootTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTestHelper
class ListCategoryIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryJpaRepository categoryRepository;

    @BeforeEach
    void mockUp(){
        Category movies = Category.newCategory("Movies", "The Greatest Movies Ever", true);
        Category series = Category.newCategory("Series", "All Series", true);
        Category documentaries = Category.newCategory("Documentaries", "All Documentaries", true);
        Category sports = Category.newCategory("Sports", null, true);
        Category kids = Category.newCategory("Kids", null, true);
        save(movies, series, documentaries, sports, kids);
    }

    @Test
    void givenAnInvalidTerm_whenCallsFindAll_thenShouldReturnAnEmptyPageOfCategories() {
        final var expectedPage = 0;
        final var expectedOffset = 3;
        final var expectedTotal = 0;
        final var expectedTerms = "asdw";
        final var expectedOrderBy = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;

        var aQuery = new SearchQuery(
                expectedPage, expectedOffset, expectedTerms, expectedOrderBy, expectedDirection
        );

        var actualResult = useCase.execute(aQuery);

        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedItemsCount, actualResult.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "vies,0,3,1,1,Movies",
            "ids,0,3,1,1,Kids",
            "ies,0,3,3,3,Documentaries"
    })
    void givenAValidTerm_whenCallsFindAll_thenShouldReturnAPageOfCategories(
            final String expectedTerms,
            final int expectedPage,
            final int expectedOffset,
            final int expectedTotal,
            final int expectedItemsCount,
            final String expectedCategoryName
    ) {
        final var expectedOrderBy = "name";
        final var expectedDirection = "asc";
        var aQuery = new SearchQuery(
                expectedPage, expectedOffset, expectedTerms, expectedOrderBy, expectedDirection
        );

        var actualResult = useCase.execute(aQuery);

        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Documentaries",
            "name,desc,0,10,5,5,Sports"
    })
    void givenAValidOrderBy_whenCallsFindAll_thenShouldReturnAPageOfCategoriesOrdered(
            final String expectedOrderBy,
            final String expectedDirection,
            final int expectedPage,
            final int expectedOffset,
            final int expectedTotal,
            final int expectedItemsCount,
            final String expectedCategoryName
    ) {
        var aQuery = new SearchQuery(
                expectedPage, expectedOffset, "", expectedOrderBy, expectedDirection
        );

        var actualResult = useCase.execute(aQuery);

        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,5,2,Documentaries;Kids",
            "1,2,5,2,Movies;Series",
            "2,2,5,1,Sports"
    })
    void givenAValidPage_whenCallsFindAll_thenShouldReturnAPageOfCategories(
            final int expectedPage,
            final int expectedOffset,
            final int expectedTotal,
            final int expectedItemsCount,
            final String expectedCategoriesNames
    ) {
        final var expectedTerms = "";
        final var expectedOrderBy = "name";
        final var expectedDirection = "asc";
        var aQuery = new SearchQuery(
                expectedPage, expectedOffset, expectedTerms, expectedOrderBy, expectedDirection
        );

        var actualResult = useCase.execute(aQuery);

        assertEquals(expectedPage, actualResult.page());
        assertEquals(expectedOffset, actualResult.offset());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedItemsCount, actualResult.items().size());
        int i = 0;
        for(final String name : expectedCategoriesNames.split(";")){
            assertEquals(name, actualResult.items().get(i).name());
            i++;
        }
    }

    private void save(final Category... category){
        categoryRepository.saveAllAndFlush(
                Arrays.stream(category).map(CategoryJpaEntity::fromAggregate).toList()
        );
    }

}
