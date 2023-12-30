package br.com.gomescarlosdev.codeflix.catalog.domain.category;

public record CategorySearchQuery(
    int page,
    int offset,
    String terms,
    String orderBy,
    String direction
){

}
