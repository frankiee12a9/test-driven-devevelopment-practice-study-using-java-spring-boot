package com.ivanfranchin.movieapi.service;

import java.util.List;

import com.ivanfranchin.movieapi.model.Tag;

public interface TagService {

    List<Tag> getTags();
    
    Tag saveTag(Tag movie);

    void deleteTag(long id);
}
