package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.api;

import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryOutput;
import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.read.GetCategoryResponse;
import br.com.gomescarlosdev.codeflix.catalog.application.category.read.GetCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.update.UpdateCategoryOutput;
import br.com.gomescarlosdev.codeflix.catalog.application.category.update.UpdateCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryID;
import br.com.gomescarlosdev.codeflix.catalog.domain.exceptions.DomainException;
import br.com.gomescarlosdev.codeflix.catalog.domain.exceptions.NotFoundException;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.Error;
import br.com.gomescarlosdev.codeflix.catalog.domain.validation.handler.Notification;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.api.CategoryApi;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.models.request.CreateCategoryRequest;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.models.request.UpdateCategoryRequest;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.helper.ControllerTestsHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTestsHelper(controllers = CategoryApi.class)
class CategoryApiTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;
    @MockBean
    private GetCategoryUseCase getCategoryUseCase;
    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryID() throws Exception {
        final var name = "Movies";
        final var description = "Most watched category";
        final var isActive = true;

        when(createCategoryUseCase.execute(any())).thenReturn(
                Right(CreateCategoryOutput.from("123"))
        );

        var request = new CreateCategoryRequest(name, description, isActive);

        this.mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(request))
                ).andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", "/v1/categories"),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.id", equalTo("123"))
                );

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(name, cmd.name()) &&
                        Objects.equals(description, cmd.description()) &&
                        Objects.equals(isActive, cmd.active())
        ));
    }

    @Test
    void givenAnInvalidCommand_whenCallsCreateCategory_shouldReturnNotification() throws Exception {
        final var description = "Most watched category";
        final var isActive = true;
        final var expectedMessage = "category 'name' should not be 'null'";

        when(createCategoryUseCase.execute(any())).thenReturn(
                Left(Notification.create(new Error(expectedMessage)))
        );

        var request = new CreateCategoryRequest(null, description, isActive);

        this.mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(request))
                ).andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location", nullValue()),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.errors[0].message", equalTo(expectedMessage))
                );
    }

    @Test
    void givenAnInvalidCommand_whenCallsCreateCategory_shouldReturnDomainException() throws Exception {
        final var description = "Most watched category";
        final var isActive = true;
        final var expectedMessage = "category 'name' should not be 'null'";

        when(createCategoryUseCase.execute(any())).thenThrow(
                DomainException.with(new Error("category 'name' should not be 'null'"))
        );

        var request = new CreateCategoryRequest(null, description, isActive);

        this.mockMvc.perform(post("/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(request))
                ).andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location", nullValue()),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.message", equalTo(expectedMessage))
                );
    }

    @Test
    void givenAValidID_whenCallsFindById_shouldReturnACategory() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedActive = true;

        final var aCategory = Category.newCategory(
                expectedName,
                expectedDescription,
                expectedActive
        );

        var expectedId = aCategory.getId().getValue();

        when(getCategoryUseCase.execute(any())).thenReturn(
                GetCategoryResponse.from(aCategory)
        );

        this.mockMvc.perform(get("/v1/categories/{id}", expectedId))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(expectedId)),
                        jsonPath("$.name", equalTo(aCategory.getName())),
                        jsonPath("$.description", equalTo(aCategory.getDescription())),
                        jsonPath("$.isActive", equalTo(aCategory.isActive())),
                        jsonPath("$.createdAt", equalTo(aCategory.getCreatedAt().toString())),
                        jsonPath("$.updatedAt", equalTo(aCategory.getUpdatedAt().toString())),
                        jsonPath("$.deletedAt").doesNotExist()
                );

        verify(getCategoryUseCase, times(1)).execute(expectedId);
    }

    @Test
    void givenAnInvalidID_whenCallsFindById_shouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = "123";

        when(getCategoryUseCase.execute(any())).thenThrow(
                NotFoundException.with(Category.class, CategoryID.from(expectedId))
        );

        this.mockMvc.perform(get("/v1/categories/{id}", expectedId))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message", equalTo(expectedErrorMessage))
                );

        verify(getCategoryUseCase, times(1)).execute(expectedId);
    }

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_thenReturnCategoryID() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedActive = true;
        final var expectedId = "123";

        when(updateCategoryUseCase.execute(any())).thenReturn(
                Right(UpdateCategoryOutput.from(expectedId))
        );

        final var request = new UpdateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedActive
        );

        this.mockMvc.perform(put("/v1/categories/{id}", expectedId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(request))
                ).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(expectedId))
                );

        verify(updateCategoryUseCase, times(1)).execute(
                argThat(category ->
                        Objects.equals(expectedId, category.id()) &&
                                Objects.equals(expectedName, category.name()) &&
                                Objects.equals(expectedDescription, category.description()) &&
                                Objects.equals(expectedActive, category.active())
                )
        );
    }

    @Test
    void givenAnInvalidId_whenCallsUpdateById_thenReturnNotFound() throws Exception {
        final var expectedMessage = "Category with ID 123 was not found";
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedActive = true;
        final var expectedId = "123";

        when(updateCategoryUseCase.execute(any())).thenThrow(
                NotFoundException.with(Category.class, CategoryID.from(expectedId))
        );

        final var request = new UpdateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedActive
        );

        this.mockMvc.perform(put("/v1/categories/{id}", expectedId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(request))
                ).andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.message", equalTo(expectedMessage))
                );

        verify(updateCategoryUseCase, times(1)).execute(
                argThat(category ->
                        Objects.equals(expectedId, category.id()) &&
                                Objects.equals(expectedName, category.name()) &&
                                Objects.equals(expectedDescription, category.description()) &&
                                Objects.equals(expectedActive, category.active())
                )
        );
    }

    @Test
    void givenAnInvalidCommand_whenCallsUpdateCategory_shouldReturnNotification() throws Exception {
        final var expectedMessage = "category 'name' should not be 'null'";
        final var expectedDescription = "Most watched category";
        final var expectedActive = true;
        final var expectedId = "123";

        when(updateCategoryUseCase.execute(any())).thenReturn(
                Left(Notification.create(new Error(expectedMessage)))
        );

        final var request = new UpdateCategoryRequest(
                null,
                expectedDescription,
                expectedActive
        );

        this.mockMvc.perform(put("/v1/categories/{id}", expectedId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(request))
                ).andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        jsonPath("$.errors[0].message", equalTo(expectedMessage))
                );

        verify(updateCategoryUseCase, times(1)).execute(any());
    }

}
