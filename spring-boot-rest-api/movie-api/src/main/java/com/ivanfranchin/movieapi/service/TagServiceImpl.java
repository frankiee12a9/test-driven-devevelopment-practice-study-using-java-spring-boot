package com.ivanfranchin.movieapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ivanfranchin.movieapi.model.Tag;
import com.ivanfranchin.movieapi.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag saveTag(Tag movie) {
        return tagRepository.save(movie);
    }

    @Override
    public void deleteTag(long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        tagRepository.delete(tag.get());
    }
}
