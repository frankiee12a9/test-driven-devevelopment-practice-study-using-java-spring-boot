package com.ivanfranchin.movieapi.rest.dto.moviereview;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMovieReviewRequest {

    @NotNull
    @DecimalMax("5.0") @DecimalMin("1.0")
    private Double score;

    @Size(min = 10, message = "Review text must be minimum 10 characters")
    private String text;

    private String createdBy;
}
