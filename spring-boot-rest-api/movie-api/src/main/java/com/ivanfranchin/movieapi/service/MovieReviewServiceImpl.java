package com.ivanfranchin.movieapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ivanfranchin.movieapi.exception.ResourceNotFoundException;
import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.model.MovieReview;
import com.ivanfranchin.movieapi.model.User;
import com.ivanfranchin.movieapi.repository.MovieRepository;
import com.ivanfranchin.movieapi.repository.MovieReviewRepository;
import com.ivanfranchin.movieapi.repository.UserRepository;
import com.ivanfranchin.movieapi.rest.dto.CreateMovieReviewRequest;
import com.ivanfranchin.movieapi.security.CustomUserDetails;

@Service
public class MovieReviewServiceImpl implements MovieReviewService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieReviewRepository reviewRepository;

    @Override
    public MovieReview addReview(CreateMovieReviewRequest reviewRequest, Long movieId, CustomUserDetails currentUserDetails) {
        Movie movie = movieRepository.findById(movieId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", movieId));
        User user = userRepository.findByUsername(currentUserDetails.getUsername())
                                    .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUserDetails.getUsername()));

        MovieReview review = new MovieReview(reviewRequest.getScore(), reviewRequest.getText());
        review.setUser(user);
        review.setMovie(movie);
        review.setScore(reviewRequest.getScore());
        review.setText(reviewRequest.getText());
        return reviewRepository.save(review);
    }

    @Override
    public Iterable<MovieReview> getReviews(Long movieId) {
        Iterable<MovieReview> reviews = reviewRepository.findByMovieId(movieId);
        return reviews;
    }
}
