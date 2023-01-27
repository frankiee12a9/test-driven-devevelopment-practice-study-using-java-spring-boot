package com.ivanfranchin.movieapi.service;

import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.model.payload.PagedResponse;
import com.ivanfranchin.movieapi.rest.dto.CreateMovieRequest;
import com.ivanfranchin.movieapi.rest.dto.CreateMovieResponse;


public interface MovieService {

    PagedResponse<Movie> getMovies(int page, int size);

    PagedResponse<Movie> getMoviesContainingText(String text, int page, int size);

    Movie validateAndGetMovie(String imdb);

    CreateMovieResponse saveMovie(CreateMovieRequest createMovieRequest);

    void deleteMovie(Movie movie);
}
