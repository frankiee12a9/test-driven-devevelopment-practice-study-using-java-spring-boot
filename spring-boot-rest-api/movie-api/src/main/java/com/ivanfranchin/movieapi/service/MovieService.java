package com.ivanfranchin.movieapi.service;

import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.model.User;
import com.ivanfranchin.movieapi.model.payload.PagedResponse;
import com.ivanfranchin.movieapi.rest.dto.movie.CreateMovieRequest;
import com.ivanfranchin.movieapi.rest.dto.movie.CreateMovieResponse;

public interface MovieService {

    PagedResponse<Movie> getMovies(int page, int size);

    PagedResponse<Movie> getMoviesContainingText(String text, int page, int size);

    Movie validateAndGetMovie(String imdb);

    CreateMovieResponse saveMovie(CreateMovieRequest createMovieRequest, User user);

    CreateMovieResponse editMovie(Long id, CreateMovieRequest editMovieRequest, User user);

    void deleteMovie(Movie movie);
}
