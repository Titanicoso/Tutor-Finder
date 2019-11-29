package ar.edu.itba.paw.services.utils;

import ar.edu.itba.paw.models.PagedResults;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationResultBuilder {

    public <T> PagedResults<T> getPagedResults(final List<T> results, final long total, final int page, final int pageSize) {

        int lastPage = (int) Math.ceil(total / (double)pageSize);
        if(lastPage == 0) {
            lastPage = 1;
        }
        return new PagedResults<>(results, total, page, lastPage);
    }
}
