package br.com.gomescarlosdev.codeflix.catalog.infrastructure.api;

import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.Pagination;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.models.CreateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/v1/categories")
@Tag(name = "Categories")
public interface CategoryApi {

    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created with success"),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> create(@RequestBody CreateCategoryRequest request);

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces =  MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> update();

    @DeleteMapping(
            produces =  MediaType.APPLICATION_JSON_VALUE,
            path = "/{categoryId}"
    )
    ResponseEntity<?> deleteById(@PathVariable String categoryId);

    @GetMapping(
            path = "/{categoryId}",
            produces =  MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> getCategoryById(@PathVariable String categoryId);


    @Operation(summary = "List all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "An invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(
            produces =  MediaType.APPLICATION_JSON_VALUE
    )
    Pagination<?> listAllCategories(
            @RequestParam(name = "term", defaultValue = "", required = false) String term,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "offset", defaultValue = "10", required = false) int offset,
            @RequestParam(name = "orderBy", defaultValue = "name", required = false) String orderBy,
            @RequestParam(name = "direction", defaultValue = "asc", required = false) String direction

    );

}
