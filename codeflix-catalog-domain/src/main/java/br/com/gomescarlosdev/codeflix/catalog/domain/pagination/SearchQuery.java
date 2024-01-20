package br.com.gomescarlosdev.codeflix.catalog.domain.pagination;

public record SearchQuery(
    int page,
    int offset,
    String terms,
    String orderBy,
    String direction
){

}
