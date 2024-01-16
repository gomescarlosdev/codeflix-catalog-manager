package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.api;

import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryOutput;
import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.api.CategoryApi;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.models.CreateCategoryRequest;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.helper.ControllerTestsHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static io.vavr.API.Right;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryID() throws Exception {
        final String url = "/v1/categories";
        final var name = "Movies";
        final var description = "Most watched category";
        final var isActive = true;

        when(createCategoryUseCase.execute(any())).thenReturn(
                Right(CreateCategoryOutput.from("123"))
        );

        var request = new CreateCategoryRequest(name, description, isActive);

        this.mockMvc.perform(post(url)
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

}
