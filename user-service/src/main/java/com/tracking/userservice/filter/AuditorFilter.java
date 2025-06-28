package com.tracking.userservice.filter;

import com.tracking.userservice.util.RequestContextHolderUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuditorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String username = request.getHeader("X-Username");
            if (username != null && !username.isBlank()) {
                RequestContextHolderUtil.setUsername(username);
            }
            filterChain.doFilter(request, response);
        } finally {
            RequestContextHolderUtil.clear();
        }
    }
}


