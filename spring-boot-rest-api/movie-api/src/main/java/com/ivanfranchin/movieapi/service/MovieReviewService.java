package com.ivanfranchin.movieapi.service;

import com.ivanfranchin.movieapi.exception.UnauthorizedException;
import com.ivanfranchin.movieapi.model.MovieReview;
import com.ivanfranchin.movieapi.model.User;
import com.ivanfranchin.movieapi.model.payload.ApiResponse;
import com.ivanfranchin.movieapi.rest.dto.moviereview.CreateMovieReviewRequest;
import com.ivanfranchin.movieapi.rest.dto.moviereview.CreateMovieReviewResponse;

public interface MovieReviewService {

    public CreateMovieReviewResponse addReview(CreateMovieReviewRequest reviewRequest, Long movieId, User currentUser);   

    Iterable<MovieReview> getReviews(Long movieId);

    public MovieReview editReview(MovieReview newReview, Long id) throws UnauthorizedException;

    public ApiResponse deleteReview(Long id) throws UnauthorizedException;
}
