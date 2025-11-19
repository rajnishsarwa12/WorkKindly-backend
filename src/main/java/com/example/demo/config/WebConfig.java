package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expose the "uploads" directory so browser can access it
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/") // folder path relative to project root
                .setCachePeriod(3600); // optional caching (1 hour)
    }
}
