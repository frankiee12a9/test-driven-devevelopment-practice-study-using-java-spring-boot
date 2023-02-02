package com.ivanfranchin.movieapi.movie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*; // is()
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanfranchin.movieapi.exception.ErrorResult;
import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.repository.MovieRepository;
import com.ivanfranchin.movieapi.rest.dto.movie.CreateMovieRequest;
import com.ivanfranchin.movieapi.service.MovieService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@Transactional
public class MovieControllerTest {
    
    @PersistenceContext
    private EntityManager entityManager;

    private static MockHttpServletRequest mockHttpRequest;

    @Mock
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CreateMovieRequest createMovieRequest;

    @Autowired
    private JdbcTemplate jdbc;   

    @Value("${sql.script.insert.movie}")
    private String sqlInsertMovie;

    @Value("${sql.script.delete.movies}")
    private String sqlDeleteMovie;

    @Value("${sql.script.insert.user}")
    private String sqlInsertUser;

    @Value("${sql.script.delete.users}")
    private String sqlDeleteUser;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 30;
    private static final int TOTAL_ELEMENTS  = 1;
    private static final int TOTAL_PAGES = 1;
    private static final int TOTAL_MOVIES = 10;
    private static final boolean LAST = true;


    @BeforeAll
    static void setupMock() {
        mockHttpRequest = new MockHttpServletRequest();

        mockHttpRequest.setParameter("imdb", "tt0163111");
        mockHttpRequest.setParameter("title", "today, now");
        mockHttpRequest.setParameter("poster", "admin");
    }

    @BeforeEach
    void setupDbBeforeTransactions() throws Exception {
        jdbc.execute(sqlInsertMovie);
        jdbc.execute(sqlInsertUser);
    }

    @Test
    void getNumberOfMoviesHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/public/numberOfMovies"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", is(TOTAL_MOVIES)));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void getMovieByGivenTextHttpRequest() throws Exception {
        String text = "home";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies")
                    .param("text", text))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page", is(DEFAULT_PAGE_NUMBER)))
                .andExpect(jsonPath("$.size", is(DEFAULT_PAGE_SIZE)))
                .andExpect(jsonPath("$.totalElements", is(TOTAL_ELEMENTS)))
                .andExpect(jsonPath("$.totalPages", is(TOTAL_PAGES)))
                .andExpect(jsonPath("$.last", is(LAST)))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void createInvalidMovieHttpRequest_ThenReturn400StatusCodeAndErrorResult() throws Exception {
        String blankImdb = "";
        String title = "test title";
        String poster = "test poster";

        createMovieRequest.setImdb(blankImdb);
        createMovieRequest.setTitle(title);
        createMovieRequest.setPoster(poster);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/movies") 
                .contentType(MediaType.APPLICATION_JSON)                       
                .content(objectMapper.writeValueAsString(createMovieRequest)))                 
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andReturn(); 

        ErrorResult  expectedErrorResponse = new ErrorResult("imdb", "must not be blank");
        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(expectedErrorResponse);
        assertTrue(actualResponse.equals(expectedResponse), "should return true");
    }

    @Test
    @WithUserDetails("admin")
    void createMovieHttpRequest() throws Exception {
        String imdb = "tt0163111";
        String title = "today, now";
        String poster = "admin";
        var associatedTags = Arrays.asList("test1", "test2");

        createMovieRequest.setImdb(imdb);
        createMovieRequest.setTitle(title);
        createMovieRequest.setPoster(poster);
        createMovieRequest.setTags(associatedTags);

        assertFalse(movieRepository.findByImdb(imdb).isPresent(), "should return false");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMovieRequest))) 
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.imdb", is(imdb)))
            .andExpect(jsonPath("$.title", is(title)))
            .andExpect(jsonPath("$.poster", is(poster)))
            .andExpect(jsonPath("$.tags", is(associatedTags)))
            .andDo(print());

        assertTrue(movieRepository.findByImdb(imdb).isPresent(), "should return true");
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void deleteNonExistingMovieByImdbHttpRequest() throws Exception {
        String imdb = "nonPresentImdb";

        assertFalse(movieRepository.findByImdb(imdb).isPresent());
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/movies/{imdb}", imdb))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void deleteMovieByImdbHttpRequest() throws Exception {
        String imdb = "tt01171998";

        assertTrue(movieRepository.findByImdb(imdb).isPresent(), "should return true");
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/movies/{imdb}", imdb))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imdb", is(imdb)))
                .andExpect(jsonPath("$.title", is("No home away"))) 
                .andExpect(jsonPath("$.poster", is("cudayanh1")))
                .andDo(print());
            
        assertFalse(movieRepository.findByImdb(imdb).isPresent(), "should return false");
    }

    @Test
    @WithUserDetails("user")
    void editMovieHttpRequest() throws Exception {
        String newImdb = "tt0163111";
        String newTitle = "today, now(edit)";
        String newPoster = "admin";
        var newAssociatedTags = Arrays.asList("test1", "test2", "test3");

        createMovieRequest.setImdb(newImdb);
        createMovieRequest.setTitle(newTitle);
        createMovieRequest.setPoster(newPoster);
        createMovieRequest.setTags(newAssociatedTags);

        Optional<Movie> movie = movieRepository.findById(6L);
        assertTrue(movie.isPresent(), "should return true");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/movies/{id}/edit", movie.get().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMovieRequest))) 
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.imdb", is(newImdb)))
            .andExpect(jsonPath("$.title", is(newTitle)))
            .andExpect(jsonPath("$.poster", is(newPoster)))
            .andExpect(jsonPath("$.tags", is(newAssociatedTags)))
            .andDo(print());
    }

    @AfterEach
    void setupDbAfterTransactions() throws Exception {
        jdbc.execute(sqlDeleteMovie);
        jdbc.execute(sqlDeleteUser);
    }
}
