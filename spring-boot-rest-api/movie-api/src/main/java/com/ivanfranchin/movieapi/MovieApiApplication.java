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
import com.ivanfranchin.movieapi.model.MovieReview;
import com.ivanfranchin.movieapi.model.User;
import com.ivanfranchin.movieapi.rest.dto.movie.CreateMovieRequest;
import com.ivanfranchin.movieapi.rest.dto.movie.CreateMovieResponse;
import com.ivanfranchin.movieapi.rest.dto.moviereview.CreateMovieReviewRequest;
import com.ivanfranchin.movieapi.rest.dto.moviereview.CreateMovieReviewResponse;
import com.ivanfranchin.movieapi.rest.dto.user.LoginRequest;
import com.ivanfranchin.movieapi.rest.dto.user.SignUpRequest;

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
    @Scope(value = "prototype")
    CreateMovieReviewRequest getCreateMovieReviewRequest() {
        return new CreateMovieReviewRequest();
    }

    @Bean
    @Scope(value = "prototype")
    CreateMovieReviewResponse getCreateMovieReviewResponse() {
        return new CreateMovieReviewResponse();
    }

    @Bean
    @Scope(value = "prototype")
    MovieReview getMovieReview() {
        return new MovieReview();
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
