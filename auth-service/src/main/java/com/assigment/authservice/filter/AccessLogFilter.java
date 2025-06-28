package com.assigment.authservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class AccessLogFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logRequestAndResponse(requestWrapper, responseWrapper, duration);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequestAndResponse(ContentCachingRequestWrapper request,
                                       ContentCachingResponseWrapper response,
                                       long duration) {
        Map<String, Object> logMap = new LinkedHashMap<>();
        logMap.put("clientIp", request.getRemoteAddr());
        logMap.put("method", request.getMethod());
        logMap.put("path", request.getRequestURI());
        logMap.put("parameters", request.getQueryString() != null ? request.getQueryString() : "{}");
        logMap.put("statusCode", response.getStatus());
        logMap.put("timeMs", duration);

        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);

        logMap.put("requestBody", Map.of(
                "contentType", request.getContentType() != null ? request.getContentType() : "N/A",
                "body", requestBody.isEmpty() ? "{}" : requestBody
        ));

        logMap.put("responseBody", Map.of(
                "contentType", response.getContentType() != null ? response.getContentType() : "N/A",
                "body", responseBody.isEmpty() ? "{}" : responseBody
        ));

        try {
            String jsonLog = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(logMap);
            logger.info(jsonLog);
        } catch (Exception e) {
            logger.error("Failed to log access log", e);
        }
    }
}
