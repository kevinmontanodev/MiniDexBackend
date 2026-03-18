package org.kmontano.minidex.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AdminApiKeyFilter extends OncePerRequestFilter {
    private final String adminKey;

    public AdminApiKeyFilter(String adminKey) {
        this.adminKey = adminKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (!path.startsWith("/api/") || !path.contains("/admin/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!request.getMethod().equals("POST")){
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        String key = request.getHeader("X-ADMIN-KEY");

        if (!adminKey.equals(key)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

