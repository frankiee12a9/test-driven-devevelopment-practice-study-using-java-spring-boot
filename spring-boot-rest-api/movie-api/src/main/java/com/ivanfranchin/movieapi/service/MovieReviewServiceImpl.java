package com.ivanfranchin.movieapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ivanfranchin.movieapi.exception.ResourceNotFoundException;
import com.ivanfranchin.movieapi.exception.UnauthorizedException;
import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.model.MovieReview;
import com.ivanfranchin.movieapi.model.User;
import com.ivanfranchin.movieapi.model.payload.ApiResponse;
import com.ivanfranchin.movieapi.repository.MovieRepository;
import com.ivanfranchin.movieapi.repository.MovieReviewRepository;
import com.ivanfranchin.movieapi.repository.UserRepository;
import com.ivanfranchin.movieapi.rest.dto.moviereview.CreateMovieReviewRequest;
import com.ivanfranchin.movieapi.rest.dto.moviereview.CreateMovieReviewResponse;
import com.ivanfranchin.movieapi.security.CustomUserDetails;

@Service
public class MovieReviewServiceImpl implements MovieReviewService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Override
    public CreateMovieReviewResponse addReview(CreateMovieReviewRequest reviewRequest, Long movieId, CustomUserDetails currentUser) {
        Movie movie = movieRepository.findById(movieId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));
        User user = userRepository.findByUsername(currentUser.getUsername())
                                    .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));

        MovieReview review = new MovieReview();
        review.setUser(user);
        review.setMovie(movie);
        review.setScore(reviewRequest.getScore());
        review.setText(reviewRequest.getText());
        review.setCreatedBy(currentUser.getUsername());
        MovieReview savedReview = movieReviewRepository.save(review);
        
        var createMovieResponse = new CreateMovieReviewResponse();
        createMovieResponse.setScore(savedReview.getScore());
        createMovieResponse.setText(savedReview.getText());
        createMovieResponse.setCreatedBy(savedReview.getCreatedBy());
        createMovieResponse.setCreatedAt(savedReview.getCreatedAt());

        return createMovieResponse;
    }

    @Override
    public Iterable<MovieReview> getReviews(Long movieId) {
        Iterable<MovieReview> reviews = movieReviewRepository.findByMovieId(movieId);
        return reviews;
    }

    @Override
    public MovieReview editReview(MovieReview newReview, Long id)
            throws UnauthorizedException {
        MovieReview reviewToEdit =  movieReviewRepository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("MovieReview", "id", id)); 
            reviewToEdit.setScore(newReview.getScore());
            reviewToEdit.setText(newReview.getText());
            return movieReviewRepository.save(reviewToEdit);
    }

    @Override
    public ApiResponse deleteReview(Long id) throws UnauthorizedException {
        MovieReview reviewToDelete = movieReviewRepository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("MovieReview", "id", id));
        movieReviewRepository.delete(reviewToDelete);
        return new ApiResponse(Boolean.TRUE, "review has been successfully deleted.");
    }
}
