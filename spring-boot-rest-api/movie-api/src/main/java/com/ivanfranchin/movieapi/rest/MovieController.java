package com.ivanfranchin.movieapi.rest;

import com.ivanfranchin.movieapi.exception.ResourceNotFoundException;
import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.model.User;
import com.ivanfranchin.movieapi.model.payload.PagedResponse;
import com.ivanfranchin.movieapi.rest.dto.movie.CreateMovieRequest;
import com.ivanfranchin.movieapi.rest.dto.movie.CreateMovieResponse;
import com.ivanfranchin.movieapi.security.CurrentUser;
import com.ivanfranchin.movieapi.security.CustomUserDetails;
import com.ivanfranchin.movieapi.service.MovieService;
import com.ivanfranchin.movieapi.service.UserService;
import com.ivanfranchin.movieapi.utils.ApplicationConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.ivanfranchin.movieapi.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final UserService userService;

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    public ResponseEntity<PagedResponse<Movie>> getMovies(@RequestParam(value = "text", required = false) String text,
        @RequestParam(value = "page", required = false, defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER) Integer page,
        @RequestParam(value = "size", required = false, defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE) Integer size) {

        PagedResponse<Movie> response = movieService.getMovies(page, size);
		return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PostMapping
    public ResponseEntity<CreateMovieResponse> createMovie(@Valid @RequestBody CreateMovieRequest createMovieRequest, 
        @AuthenticationPrincipal CustomUserDetails currentUser) {

        Optional<User> user = userService.getUserByUsername(currentUser.getUsername());
        CreateMovieResponse response = movieService.saveMovie(createMovieRequest, user.get());
		return new ResponseEntity< >(response, HttpStatus.CREATED);
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/{imdb}")
    public Movie deleteMovie(@PathVariable String imdb) {

        Movie movie = movieService.validateAndGetMovie(imdb);
        movieService.deleteMovie(movie);
        return movie;
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PutMapping("/{id}/edit")
    public ResponseEntity<CreateMovieResponse> editMovie(@PathVariable Long id, @Valid @RequestBody CreateMovieRequest editMovieRequest, 
        @AuthenticationPrincipal CustomUserDetails currentUser) {

        Optional<User> user = userService.getUserByUsername(currentUser.getUsername());
        CreateMovieResponse movie = movieService.editMovie(id, editMovieRequest, user.get());
        return new ResponseEntity<>(movie, HttpStatus.CREATED); // `201 status` if new resource is returned, otherwise `204` with no-content
    }
}
