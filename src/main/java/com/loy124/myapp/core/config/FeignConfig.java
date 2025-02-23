package com.loy124.myapp.core.config;

import feign.auth.BasicAuthRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignConfig {


    @Qualifier(value = "mailgun")
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor( @Value("${mailgun.apikey}") String key) {
//        return new BasicAuthRequestInterceptor("api", key);
                return new BasicAuthRequestInterceptor("api", key);

    }


}
