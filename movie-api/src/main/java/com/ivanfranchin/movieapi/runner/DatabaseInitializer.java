package com.ivanfranchin.movieapi.runner;

import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.model.MovieReview;
import com.ivanfranchin.movieapi.model.Tag;
import com.ivanfranchin.movieapi.model.User;
import com.ivanfranchin.movieapi.rest.dto.CreateMovieRequest;
import com.ivanfranchin.movieapi.security.oauth2.OAuth2Provider;
import com.ivanfranchin.movieapi.security.WebSecurityConfig;
import com.ivanfranchin.movieapi.service.MovieService;
import com.ivanfranchin.movieapi.service.TagService;
import com.ivanfranchin.movieapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final MovieService movieService;
    private final TagService tagService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userService.getUsers().isEmpty()) {
            return;
        }
        TAGS.forEach(tagService::saveTag);
        USERS.forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        });
        MOVIES.forEach(movieService::saveMovie);
        log.info(">> LOGGING: Database initialized");
    }

    private static final List<User> USERS = Arrays.asList(
            new User("admin", "admin", "Admin", "admin@mycompany.com", WebSecurityConfig.ADMIN, null, OAuth2Provider.LOCAL, "1"),
            new User("user", "user", "User", "user@mycompany.com", WebSecurityConfig.USER, null, OAuth2Provider.LOCAL, "2")
    );
    
    private static final List<Tag> TAGS = Arrays.asList(
        new Tag("comedy", "description about comedy"),
        new Tag("romance", "description about romance")
        // new Tag("war", ""),
        // new Tag("horror", ""),
        // new Tag("action", ""),
        // new Tag("fantasy", "")
    );

    private static final List<CreateMovieRequest> MOVIES = Arrays.asList(
            new CreateMovieRequest("tt11234", "just a test", "https://m.media-amazon.com/images/M/MV5BMjI5MDY1NjYzMl5BMl5BanBnXkFtZTgwNjIzNDAxNDM@._V1_SX300.jpg"), 
            new CreateMovieRequest("tt11235", "just a test2", "https://m.media-amazon.com/images/M/MV5BYTE1ZTBlYzgtNmMyNS00ZTQ2LWE4NjEtZjUxNDJkNTg2MzlhXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg")
            
    );
}
