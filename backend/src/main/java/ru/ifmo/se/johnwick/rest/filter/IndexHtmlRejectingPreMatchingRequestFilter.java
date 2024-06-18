package ru.ifmo.se.johnwick.rest.filter;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import ru.ifmo.se.johnwick.constant.ApiConstant;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@PreMatching
@Provider
@Priority(Integer.MIN_VALUE)
public class IndexHtmlRejectingPreMatchingRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        if (containerRequestContext.getUriInfo().getPath().startsWith(ApiConstant.API) || !containerRequestContext.getRequest().getMethod().equals("GET")) {
            return;
        }

        try {
            final InputStream indexIS = this.getClass().getClassLoader().getResourceAsStream("/META-INF/resources/index.html");
            final InputStreamReader isr = new InputStreamReader(indexIS, StandardCharsets.UTF_8);
            final StringBuilder sb = new StringBuilder();
            int c;
            while ((c = isr.read()) != -1) {
                sb.append((char) c);
            }
            containerRequestContext.abortWith(
                    Response
                            .ok(sb.toString(), MediaType.TEXT_HTML_TYPE)
                            .build()
            );
        } catch (Exception ex) {
            containerRequestContext.abortWith(
                    Response.temporaryRedirect(URI.create("/")).build()
            );
        }
    }
}
