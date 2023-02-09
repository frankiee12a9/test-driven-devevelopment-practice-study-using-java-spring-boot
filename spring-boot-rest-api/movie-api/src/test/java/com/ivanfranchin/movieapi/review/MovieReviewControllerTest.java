package com.ivanfranchin.movieapi.review;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*; // is()
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanfranchin.movieapi.exception.ErrorResult;
import com.ivanfranchin.movieapi.model.MovieReview;
import com.ivanfranchin.movieapi.repository.MovieRepository;
import com.ivanfranchin.movieapi.repository.MovieReviewRepository;
import com.ivanfranchin.movieapi.rest.dto.moviereview.CreateMovieReviewRequest;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc()
@DisplayName("Testing MovieReviewController")
@SpringBootTest
@Transactional
public class MovieReviewControllerTest {
    
    private static MockHttpServletRequest mockHttpRequest;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbc;   

    @Autowired
    private CreateMovieReviewRequest createMovieReviewRequest;
    
    @Value("${sql.script.insert.movie_review}")
    private String sqlInsertMovieReview;

    @Value("${sql.script.insert.movie}")
    private String sqlInsertMovie;

    @Value("${sql.script.insert.user}")
    private String sqlInsertUser;

    @Value("${sql.script.delete.movie_reviews}")
    private String sqlDeleteMovieReviews;

    @Value("${sql.script.delete.movies}")
    private String sqlDeleteMovies;

    @Value("${sql.script.delete.users}")
    private String sqlDeleteUsers;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;


    @BeforeAll
    static void setupMock() {
        mockHttpRequest = new MockHttpServletRequest();
    }

    @BeforeEach
    void setupDbBeforeTransactions() throws Exception {
        jdbc.execute(sqlInsertUser);
        jdbc.execute(sqlInsertMovie);
        jdbc.execute(sqlInsertMovieReview);
    }
   
    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void testGetMovieReviewsFromSpecificMovieHttpRequest() throws Exception {
        long movieId = 6l;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/{movieId}/reviews", movieId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print());   
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    public void testAddInvalidMovieReviewHttpRequest_ThenReturnErrorResultWith401StatusCode() throws Exception {
        String movieId = "6";
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elitt";
        createMovieReviewRequest.setText(text);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/{movieId}/reviews", movieId) 
                .contentType(MediaType.APPLICATION_JSON)                       
                .content(objectMapper.writeValueAsString(createMovieReviewRequest)))                 
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andReturn(); 

        ErrorResult  expectedErrorResponse = new ErrorResult("score", "must not be null");
        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(expectedErrorResponse);
        assertTrue(actualResponse.equals(expectedResponse), "should return true");
    }

    @Test
    @WithUserDetails("user")
    public void testAddMovieReviewHttpRequest() throws Exception {
        long movieId = 6l;
        Double score = 4.5;
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut";
        String createdBy = "user";

        createMovieReviewRequest.setScore(score);
        createMovieReviewRequest.setText(text);
        createMovieReviewRequest.setCreatedBy(createdBy);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/{movieId}/reviews", movieId) 
                .contentType(MediaType.APPLICATION_JSON)                       
                .content(objectMapper.writeValueAsString(createMovieReviewRequest)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.score", is(score)))
            .andExpect(jsonPath("$.text", is(text)))
            .andExpect(jsonPath("$.createdBy", is("user")))
            .andDo(print());
    }

    @Test
    @WithUserDetails("user")
    void testEditMovieReviewHttpRequest() throws Exception {
        long movieId = 6l;
        assertTrue(movieRepository.findById(movieId).isPresent(), "should return true");

        long reviewId = 6l;
        Optional <MovieReview> review = movieReviewRepository.findById(reviewId);
        assertTrue(review.isPresent(), "should return true");

        // edit score
        Double editedScore = 3.5;
        createMovieReviewRequest.setScore(editedScore);
        
        mockMvc.perform(MockMvcRequestBuilders.put("/api/movies/{movieId}/reviews/{id}", movieId, reviewId) 
        .contentType(MediaType.APPLICATION_JSON)                       
        .content(objectMapper.writeValueAsString(createMovieReviewRequest)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.score", is(editedScore)))
        .andExpect(jsonPath("$.text", is(review.get().getText())))
        .andExpect(jsonPath("$.createdBy", is(review.get().getCreatedBy())))
        .andDo(print());

        // verify edited score result
        review = movieReviewRepository.findById(reviewId);
        assertEquals(editedScore, review.get().getScore(), "should return equals");

        // edit text 
        String editedText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit";
        createMovieReviewRequest.setText(editedText);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/movies/{movieId}/reviews/{id}", movieId, reviewId) 
                .contentType(MediaType.APPLICATION_JSON)                       
                .content(objectMapper.writeValueAsString(createMovieReviewRequest)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.score", is(review.get().getScore())))
            .andExpect(jsonPath("$.text", is(editedText)))
            .andExpect(jsonPath("$.createdBy", is(review.get().getCreatedBy())))
            .andDo(print());
        
        // verify edited text result
        review = movieReviewRepository.findById(reviewId);
        assertEquals(editedText, review.get().getText(), "should return equals");
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    void testDeleteMovieReviewHttpRequest() throws Exception {
        long movieId = 6l;
        assertNotNull(movieRepository.findById(movieId).get(), "should not be null");

        long reviewId = 6l;
        Optional<MovieReview> review = movieReviewRepository.findById(reviewId);
        assertTrue(review.isPresent(), "should return true");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/movies/{movieId}/reviews/{id}", movieId, reviewId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.success", is(Boolean.TRUE)))
                .andExpect(jsonPath("$.message", is("review has been successfully deleted.")))
                .andDo(print())
                .andReturn();

        review = movieReviewRepository.findById(reviewId);
        assertFalse(review.isPresent(), "should return false");
    }

    @AfterEach
    void setupDbAfterTransactions() throws Exception {
        jdbc.execute(sqlDeleteMovieReviews);
        jdbc.execute(sqlDeleteMovies);
        jdbc.execute(sqlDeleteUsers);
    }
}


