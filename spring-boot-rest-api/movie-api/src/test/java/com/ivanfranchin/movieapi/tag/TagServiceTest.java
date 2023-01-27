package com.ivanfranchin.movieapi.tag;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

// @RunWith(SpringRunner.class)
// @DataJdbcTest
@TestPropertySource("/application-test.properties")
@SpringBootTest
public class TagServiceTest {
    
}
