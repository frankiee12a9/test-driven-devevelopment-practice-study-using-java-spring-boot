package com.ivanfranchin.movieapi.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ivanfranchin.movieapi.model.MovieReview;
import com.ivanfranchin.movieapi.rest.dto.CreateMovieReviewRequest;
import com.ivanfranchin.movieapi.security.CurrentUser;
import com.ivanfranchin.movieapi.security.CustomUserDetails;
import com.ivanfranchin.movieapi.service.MovieReviewService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static com.ivanfranchin.movieapi.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movies/{movieId}/reviews")
public class MovieReviewController {
    
    private final MovieReviewService reviewService;

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    public ResponseEntity<Iterable<MovieReview>> getReviews(@PathVariable(name = "movieId") Long movieId) {
        Iterable<MovieReview> reviews = reviewService.getReviews(movieId);
        return new ResponseEntity<> (reviews, HttpStatus.OK);
    } 


    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PostMapping
    public ResponseEntity<MovieReview> addReview(@Valid @RequestBody CreateMovieReviewRequest createMovieReviewRequest, @PathVariable(name ="movieId") Long movieId, @CurrentUser CustomUserDetails customUserDetails) {   
        MovieReview review = reviewService.addReview(createMovieReviewRequest, movieId, customUserDetails); 
        return new ResponseEntity<> (review, HttpStatus.CREATED);
    }
}
