package com.ivanfranchin.movieapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ivanfranchin.movieapi.model.MovieReview;

@Repository
public interface MovieReviewRepository extends JpaRepository<MovieReview, Long> {
    Iterable<MovieReview> findByMovieId(Long movieId);
}
