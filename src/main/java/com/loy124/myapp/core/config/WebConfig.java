package com.loy124.myapp.core.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${file-upload.dir}")
    private String fileUploadDir;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(fileUploadInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String address = "file:" +uploadDir;
        String fileAddress = "file:" + fileUploadDir;

        registry.addResourceHandler("/public/**")
                .addResourceLocations(address);

        registry.addResourceHandler("/file/**")
                .addResourceLocations(fileAddress);
    }
}
