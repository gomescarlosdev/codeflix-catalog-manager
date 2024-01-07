package br.com.gomescarlosdev.codeflix.catalog.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    private SpecificationUtils(){

    }

    public static <T> Specification<T> like (final String columnName, final String term){
        return (root, query, cb) -> cb.like(cb.upper(root.get(columnName)), like(term));
    }

    private static String like(String term) {
        return "%" + term.toUpperCase() + "%";
    }
}
