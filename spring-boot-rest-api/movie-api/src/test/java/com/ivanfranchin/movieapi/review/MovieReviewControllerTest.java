package com.ivanfranchin.movieapi.review;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@Transactional
public class MovieReviewControllerTest {
    
}
