package com.ivanfranchin.movieapi.rest.dto.movie;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class CreateMovieResponse {
    
    private String imdb;

    private String title;

    private String poster;

    private String createdBy;

    private Instant createdAt;

    private Instant updatedAt;

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
