package com.ivanfranchin.movieapi.repository;

import com.ivanfranchin.movieapi.model.Movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findAllByOrderByTitle(Pageable pageable);

    Page<Movie> findByImdbContainingOrTitleContainingOrderByTitle(String imdb, String title, Pageable pageable);

    Optional<Movie> findByImdb(String imdb);
}
