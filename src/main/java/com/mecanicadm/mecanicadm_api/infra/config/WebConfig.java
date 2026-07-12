package com.mecanicadm.mecanicadm_api.infra.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.io.IOException;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfig {

    @Bean
    public Filter cacheControlFilter() {
        return new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                String path = ((jakarta.servlet.http.HttpServletRequest) request).getRequestURI();

                httpResponse.setHeader("Cross-Origin-Resource-Policy", "same-origin");
                httpResponse.setHeader("Content-Security-Policy", "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self'; connect-src 'self'; frame-ancestors 'none'");

                if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
                    httpResponse.setHeader("Cache-Control", "public, max-age=3600");
                } else {
                    httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
                    httpResponse.setHeader("Pragma", "no-cache");
                    httpResponse.setHeader("Cross-Origin-Opener-Policy", "same-origin");
                    httpResponse.setHeader("Permissions-Policy", "accelerometer=(), camera=(), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), payment=(), usb=()");
                }

                if (!path.startsWith("/swagger-ui")) {
                    httpResponse.setHeader("Cross-Origin-Embedder-Policy", "require-corp");
                }

                chain.doFilter(request, response);
            }
        };
    }
}
