package com.ivanfranchin.movieapi.service;

import java.nio.file.attribute.UserPrincipal;

import com.ivanfranchin.movieapi.model.MovieReview;
import com.ivanfranchin.movieapi.rest.dto.CreateMovieReviewRequest;
import com.ivanfranchin.movieapi.security.CustomUserDetails;

public interface MovieReviewService {
    public MovieReview addReview(CreateMovieReviewRequest reviewRequest, Long movieId, CustomUserDetails currentUserDetails);   
    Iterable<MovieReview> getReviews(Long movieId);
}
