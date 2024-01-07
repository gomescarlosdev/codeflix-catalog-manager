package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category;

import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryGateway;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryID;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategorySearchQuery;
import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.Pagination;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryEntity;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryRepository;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.utils.SpecificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction;

@Service
public class CategoryGatewayImpl implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryGatewayImpl(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(final Category category) {
        return save(category);
    }

    @Override
    public Category update(final Category category) {
        return save(category);
    }

    @Override
    public void deleteById(final CategoryID categoryID) {
        var categoryIdValue = categoryID.getValue();
        if (this.categoryRepository.existsById(categoryIdValue)) {
            this.categoryRepository.deleteById(categoryIdValue);
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID categoryID) {
        return this.categoryRepository.findById(
                categoryID.getValue()
        ).map(CategoryEntity::toAggregate);
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery query) {

        final var page = PageRequest.of(
                query.page(),
                query.offset(),
                Sort.by(Direction.fromString(query.direction()), query.orderBy())
        );

        final var specifications = Optional.ofNullable(query.terms())
                .filter(term -> !term.isBlank())
                .map(term -> SpecificationUtils.<CategoryEntity>like("name", term)
                        .or(SpecificationUtils.like("description", term))
                ).orElse(null);

        final var pageResult = this.categoryRepository.findAll(
                Specification.where(specifications),
                page
        );

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryEntity::toAggregate).toList()
        );
    }

    private Category save(final Category category) {
        return this.categoryRepository.save(
                CategoryEntity.fromAggregate(category)
        ).toAggregate();
    }
}
