package com.ivanfranchin.movieapi.rest.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateMovieReviewRequest {
    // @Size(min = 1, max = 5 , message = "Review score must be between 1 and 5")
    // @NotBlank
    @DecimalMax("5.0") @DecimalMin("1.0")
    private Double score;

    @Size(min = 10, message = "Review text must be minimum 10 characters")
    private String text;
}
