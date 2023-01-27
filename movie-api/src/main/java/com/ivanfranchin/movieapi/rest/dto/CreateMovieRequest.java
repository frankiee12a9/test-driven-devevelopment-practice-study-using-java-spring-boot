package com.ivanfranchin.movieapi.rest.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMovieRequest {

    @Schema(example = "tt0117998")
    @NotBlank
    private String imdb;

    @Schema(example = "Twister")
    @NotBlank
    private String title;

    @Schema(example = "https://m.media-amazon.com/images/M/MV5BODExYTM0MzEtZGY2Yy00N2ExLTkwZjItNGYzYTRmMWZlOGEzXkEyXkFqcGdeQXVyNDk3NzU2MTQ@._V1_SX300.jpg")
    @NotBlank
    private String poster;

    public CreateMovieRequest(String imdb, String title, String poster) {
        this.imdb = imdb;
        this.title = title;
        this.poster = poster;
    }

    private List <String> tags;

    public List<String> getTags() {
        return tags == null ? Collections.emptyList() : new ArrayList<String>(tags);
    }

    public void setTags(List<String> tags) {
        if (tags == null) 
            this.tags = null;
        else 
            this.tags = Collections.unmodifiableList(tags);
    }
}
