package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.PagedResults;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaginationLinkBuilder {

    public Link[] buildLinks(final UriInfo uriInfo, PagedResults results) {

        final UriBuilder uriBuilder = uriInfo.getRequestUriBuilder();
        final List<Link> links = new ArrayList<>();
        Link link;

        if(results.getPage() > 1) {
            link = Link.fromUriBuilder(uriBuilder.replaceQueryParam("page", results.getPage() - 1))
                    .rel("next")
                    .build();
            links.add(link);
        }

        if(results.getPage() < results.getLastPage()) {
            link = Link.fromUriBuilder(replacePage(uriBuilder, results.getPage() + 1))
                    .rel("next")
                    .build();
            links.add(link);
        }

        link = Link.fromUriBuilder(replacePage(uriBuilder, results.getLastPage()))
                .rel("last")
                .build();

        links.add(link);

        link = Link.fromUriBuilder(replacePage(uriBuilder, 1))
                .rel("first")
                .build();

        links.add(link);

        return links.toArray(new Link[]{});
    }

    private UriBuilder replacePage(UriBuilder builder, int page) {
        return builder.replaceQueryParam("page", page);
    }
}
