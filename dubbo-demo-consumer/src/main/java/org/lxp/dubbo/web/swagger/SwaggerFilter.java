package org.lxp.dubbo.web.swagger;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class SwaggerFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(SwaggerFilter.class);

    private final boolean swaggerAllowed;

    public SwaggerFilter(boolean swaggerAllowed) {
        this.swaggerAllowed = swaggerAllowed;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        LOG.info("path={}, swaggerAllowed={}", request.getServletPath(), swaggerAllowed);
        if (!swaggerAllowed) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Swagger is not enabled in this environment");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
