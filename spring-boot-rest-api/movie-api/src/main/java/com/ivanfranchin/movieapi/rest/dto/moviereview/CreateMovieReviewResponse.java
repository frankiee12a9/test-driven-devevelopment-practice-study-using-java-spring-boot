package com.ivanfranchin.movieapi.rest.dto.moviereview;

import java.time.Instant;

import lombok.Data;

@Data
public class CreateMovieReviewResponse {
    
    private Double score;
    private String text;
    private String createdBy;
    private Instant createdAt;
}
