package com.ivanfranchin.movieapi.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ivanfranchin.movieapi.model.MovieReview;
import com.ivanfranchin.movieapi.model.payload.ApiResponse;
import com.ivanfranchin.movieapi.rest.dto.moviereview.CreateMovieReviewRequest;
import com.ivanfranchin.movieapi.rest.dto.moviereview.CreateMovieReviewResponse;
import com.ivanfranchin.movieapi.security.CustomUserDetails;
import com.ivanfranchin.movieapi.service.MovieReviewService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<CreateMovieReviewResponse> addReview(@Valid @RequestBody CreateMovieReviewRequest createMovieReviewRequest, 
        @PathVariable(name ="movieId") Long movieId, @AuthenticationPrincipal CustomUserDetails currentUser) {   

        CreateMovieReviewResponse review = reviewService.addReview(createMovieReviewRequest, movieId, currentUser); 
        return new ResponseEntity<> (review, HttpStatus.CREATED);
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PutMapping("/{id}")
    public ResponseEntity<MovieReview> editReview(@PathVariable(name = "id") Long id, @Valid @RequestBody MovieReview movieReview) {   

        MovieReview review = reviewService.editReview(movieReview, id); 
        return new ResponseEntity<> (review, HttpStatus.CREATED);
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable(name = "id") Long id) {   

        ApiResponse apiResponse = reviewService.deleteReview(id); 
        return new ResponseEntity<> (apiResponse, HttpStatus.OK);
    }
}

