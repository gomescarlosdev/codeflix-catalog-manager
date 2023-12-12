package br.com.gomescarlosdev.domain.pagination;

import java.util.List;

public record Pagination<T>(
        int page,
        int offset,
        long limit,
        List<T> items
) {

}
