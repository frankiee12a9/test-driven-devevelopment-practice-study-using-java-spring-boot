package com.ivanfranchin.movieapi.rest;

import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.model.payload.PagedResponse;
import com.ivanfranchin.movieapi.rest.dto.CreateMovieRequest;
import com.ivanfranchin.movieapi.rest.dto.CreateMovieResponse;
import com.ivanfranchin.movieapi.service.MovieService;
import com.ivanfranchin.movieapi.utils.ApplicationConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.ivanfranchin.movieapi.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    public ResponseEntity<PagedResponse<Movie>> getMovies(@RequestParam(value = "text", required = false) String text,
        @RequestParam(value = "page", required = false, defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER) Integer page,
        @RequestParam(value = "size", required = false, defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE) Integer size)
    {
        PagedResponse<Movie> response = movieService.getMovies(page, size);
		return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CreateMovieResponse createMovie(@Valid @RequestBody CreateMovieRequest createMovieRequest) {
        CreateMovieResponse response = movieService.saveMovie(createMovieRequest);
        return response;
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/{imdb}")
    public Movie deleteMovie(@PathVariable String imdb) {
        Movie movie = movieService.validateAndGetMovie(imdb);
        movieService.deleteMovie(movie);
        return movie;
    }
}
