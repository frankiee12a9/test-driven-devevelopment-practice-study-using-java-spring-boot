package com.ivanfranchin.movieapi;

import java.util.TimeZone;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.convert.Jsr310Converters;

import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.model.User;
import com.ivanfranchin.movieapi.rest.dto.CreateMovieRequest;
import com.ivanfranchin.movieapi.rest.dto.CreateMovieResponse;
import com.ivanfranchin.movieapi.rest.dto.LoginRequest;
import com.ivanfranchin.movieapi.rest.dto.SignUpRequest;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EntityScan(basePackageClasses = { MovieApiApplication.class, Jsr310Converters.class })
public class MovieApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieApiApplication.class, args);
    }
    
    @Bean
    @Scope(value = "prototype")
    User getUser() {
        return new User();
    }

    @Bean 
    @Scope(value = "prototype")
    Movie getMovie() {
        return new Movie();
    }

    @Bean
    @Scope(value = "prototype")
    LoginRequest getLoginRequest() {
        return new LoginRequest();
    }

    @Bean
    @Scope(value = "prototype")
    SignUpRequest getSignUpRequest() {
        return new SignUpRequest();
    }

    @Bean
    @Scope(value = "prototype")
    CreateMovieRequest getCreateMovieRequest() {
        return new CreateMovieRequest();
    }

    @Bean
    @Scope(value = "prototype")
    CreateMovieResponse getCreateMovieResponse() {
        return new CreateMovieResponse();
    }

    @Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

    @PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
