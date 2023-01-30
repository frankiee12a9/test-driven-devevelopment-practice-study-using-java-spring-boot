package com.ivanfranchin.movieapi.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

    @Test
    public void testPasswordEncoder() throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "password";
		String encodedPassword = passwordEncoder.encode(rawPassword);
		
		System.out.println(encodedPassword);
		
		boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        
        assertEquals(true, matches, "should return true");
    }
}
