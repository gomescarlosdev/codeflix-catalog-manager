package br.com.gomescarlosdev.codeflix.catalog.infrastructure.category;

import br.com.gomescarlosdev.codeflix.catalog.domain.category.Category;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryGateway;
import br.com.gomescarlosdev.codeflix.catalog.domain.category.CategoryID;
import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.SearchQuery;
import br.com.gomescarlosdev.codeflix.catalog.domain.pagination.Pagination;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import br.com.gomescarlosdev.codeflix.catalog.infrastructure.category.persistence.CategoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.com.gomescarlosdev.codeflix.catalog.infrastructure.utils.SpecificationUtils.like;
import static org.springframework.data.domain.Sort.Direction;

@Service
public class CategoryGatewayImpl implements CategoryGateway {

    private final CategoryJpaRepository categoryJpaRepository;

    @Autowired
    public CategoryGatewayImpl(final CategoryJpaRepository categoryRepository) {
        this.categoryJpaRepository = categoryRepository;
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
        if (this.categoryJpaRepository.existsById(categoryIdValue)) {
            this.categoryJpaRepository.deleteById(categoryIdValue);
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID categoryID) {
        return this.categoryJpaRepository.findById(
                categoryID.getValue()
        ).map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Category> findAll(final SearchQuery query) {

        final var page = PageRequest.of(
                query.page(),
                query.offset(),
                Sort.by(Direction.fromString(query.direction()), query.orderBy())
        );

        final var specifications = Optional.ofNullable(query.terms())
                .filter(term -> !term.isBlank())
                .map(term -> {
                        final Specification<CategoryJpaEntity> nameLike = like("name", term);
                        final Specification<CategoryJpaEntity> descriptionLike = like("description", term);
                        return nameLike.or(descriptionLike);
                }).orElse(null);

        final var pageResult = this.categoryJpaRepository.findAll(
                Specification.where(specifications),
                page
        );

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    private Category save(final Category category) {
        return this.categoryJpaRepository.save(
                CategoryJpaEntity.fromAggregate(category)
        ).toAggregate();
    }
}
