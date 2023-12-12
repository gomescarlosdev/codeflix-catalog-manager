package br.com.gomescarlosdev.domain.category;

public record CategorySearchQuery(
    int page,
    int offset,
    String terms,
    String orderBy,
    String direction
){

}
