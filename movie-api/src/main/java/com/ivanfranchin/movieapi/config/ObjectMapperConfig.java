package com.ivanfranchin.movieapi.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
    //! enabling this cause https://github.com/spring-projects/spring-boot/issues/26859
    // @Bean
    // public ObjectMapper objectMapper() {
    //     return new ObjectMapper();
    //     // return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // }
}
