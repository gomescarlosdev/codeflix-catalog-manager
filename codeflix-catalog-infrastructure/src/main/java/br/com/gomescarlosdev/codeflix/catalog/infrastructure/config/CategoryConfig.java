package br.com.gomescarlosdev.codeflix.catalog.infrastructure.config;

import br.com.gomescarlosdev.codeflix.catalog.application.category.create.CreateCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.create.DefaultCreateCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.delete.DefaultDeleteCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.delete.DeleteCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.read.DefaultGetCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.read.DefaultListCategoriesUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.read.GetCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.read.ListCategoriesUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.application.category.update.UpdateCategoryUseCase;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryConfig {

    private CategoryGateway categoryGateway;

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(){
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase(){
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(){
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryUseCase getCategoryUseCase(){
        return new DefaultGetCategoryUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase getCategoryListUseCase(){
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

}
