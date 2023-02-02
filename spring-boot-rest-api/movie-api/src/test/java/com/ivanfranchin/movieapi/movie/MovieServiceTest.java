package com.ivanfranchin.movieapi.movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.model.User;
import com.ivanfranchin.movieapi.repository.MovieRepository;
import com.ivanfranchin.movieapi.repository.TagRepository;
import com.ivanfranchin.movieapi.repository.UserRepository;
import com.ivanfranchin.movieapi.rest.dto.movie.CreateMovieRequest;
import com.ivanfranchin.movieapi.rest.dto.movie.CreateMovieResponse;
import com.ivanfranchin.movieapi.service.MovieService;
import com.ivanfranchin.movieapi.service.MovieServiceImpl;


@TestPropertySource("/application-test.properties")
@SpringBootTest
public class MovieServiceTest {

    @Autowired
    private JdbcTemplate jdbc;   

    @Spy
    @InjectMocks
    @Autowired
    private MovieService movieService = new MovieServiceImpl(); //? for mockito

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private User user;

    @Value("${sql.script.insert.movie}")
    private String sqlInsertMovie;

    @Value("${sql.script.insert.user}")
    private String sqlInsertUser;

    @Value("${sql.script.delete.users}")
    private String sqlDeleteUsers;

    @Value("${sql.script.delete.movies}")
    private String sqlDeleteMovie;

    @Value("${sql.script.delete.movie_tag}")
    private String sqlDeleteMovieTag;

    private static final String IMDB = "tt20230206";
    private static final String TITLE = "LARVA episodes";
    private static final String POSTER = "blablablablablablablablablablablablablablablablablablabla";
    private static final List<String> ASSOCIATED_TAGS = Arrays.asList("funny", "kids");

    @BeforeEach
    void setupDbBeforeTransactions() throws Exception {
        jdbc.execute(sqlInsertUser);
        jdbc.execute(sqlInsertMovie); 
    }

    @Test
    void isMovieNullCheck() throws Exception {
        Movie movie = movieService.validateAndGetMovie("tt01171998");
        assertEquals("tt01171998", movie.getImdb() ,"should return true for movie has `imdb: tt01171998`");
    }

    @Test
    void createMovieService() throws Exception {
        CreateMovieRequest createMovieRequest = new CreateMovieRequest("tt01171999", "spring security", "blabalbal");
        var tags = Arrays.asList("test1", "test2");
        createMovieRequest.setTags(tags);

        String username = "Kane";
        Optional<User> user = userRepository.findByUsername(username);

        assertTrue(user.isPresent(), "should return true");

        CreateMovieResponse createMovieResponse =  movieService.saveMovie(createMovieRequest, user.get());

        Movie movieToCheck = movieService.validateAndGetMovie(createMovieResponse.getImdb()); //? just for testing this service's method

        assertEquals("tt01171999", movieToCheck.getImdb());
    }

    @Test
    void deleteMovieService() throws Exception {
        Optional<Movie> movie = movieRepository.findByImdb("tt01171998");

        assertTrue(Optional.of(movie).isPresent(), "should return true");

        movieService.deleteMovie(movie.get());

        movie = movieRepository.findByImdb("tt01171998");

        assertFalse(movie.isPresent(), "should return false");
    }

    @Test
    void integrationTestMovieService() throws Exception {
        CreateMovieRequest createMovieRequest = new CreateMovieRequest(IMDB, TITLE, POSTER);
        createMovieRequest.setTags(ASSOCIATED_TAGS);

        assertFalse(movieRepository.findByImdb(createMovieRequest.getImdb()).isPresent(), "Should return false");

        String username = "Kane";
        Optional<User> user = userRepository.findByUsername(username);

        assertTrue(user.isPresent(), "should return true");

        CreateMovieResponse createMovieResponse =  movieService.saveMovie(createMovieRequest, user.get());

        assertTrue(movieRepository.findByImdb(createMovieResponse.getImdb()).isPresent(), "Should return true");

        Movie validatedMovie = movieService.validateAndGetMovie(createMovieResponse.getImdb());

        assertEquals(createMovieResponse.getImdb(), validatedMovie.getImdb(), "Should return true");

        movieService.deleteMovie(validatedMovie);

        assertTrue(movieRepository.findByImdb(validatedMovie.getImdb()).isEmpty(), "Should return true");
    }

    @Test
    void tagShouldBeMappedWithMovieAsManyToManyER() throws Exception {
        CreateMovieRequest createMovieRequest = new CreateMovieRequest(IMDB, TITLE, POSTER);
        createMovieRequest.setTags(ASSOCIATED_TAGS);

        String username = "Kane";
        Optional<User> user = userRepository.findByUsername(username);

        assertTrue(user.isPresent(), "should return true");

        CreateMovieResponse createMovieResponse = movieService.saveMovie(createMovieRequest, user.get());
        
        assertTrue(tagRepository.findAllInName(createMovieResponse.getTags()).size() == 2, "Should return true");

        for (String associatedTag : createMovieResponse.getTags()) 
            assertTrue(tagRepository.findByName(associatedTag).isPresent(), "Should return true");
    }

    @AfterEach
    void setupDbAfterTransactions() throws Exception {
        jdbc.execute(sqlDeleteMovieTag); 
        jdbc.execute(sqlDeleteMovie); 
        jdbc.execute(sqlDeleteUsers);
    }
}
