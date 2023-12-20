package br.com.gomescarlosdev.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
        int page,
        int offset,
        long total,
        List<T> items
) {
    public <R> Pagination<R> map(final Function<T, R> mapper) {
        final List<R> list = this.items.stream().map(mapper).toList();
        return new Pagination<>(page(), offset(), total(), list);
    }
}
