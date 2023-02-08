package com.ivanfranchin.movieapi.service;

import com.ivanfranchin.movieapi.exception.ResourceNotFoundException;
import com.ivanfranchin.movieapi.model.Movie;
import com.ivanfranchin.movieapi.model.Tag;
import com.ivanfranchin.movieapi.model.User;
import com.ivanfranchin.movieapi.model.payload.PagedResponse;
import com.ivanfranchin.movieapi.repository.MovieRepository;
import com.ivanfranchin.movieapi.repository.TagRepository;
import com.ivanfranchin.movieapi.repository.UserRepository;
import com.ivanfranchin.movieapi.rest.dto.movie.CreateMovieRequest;
import com.ivanfranchin.movieapi.rest.dto.movie.CreateMovieResponse;
import com.ivanfranchin.movieapi.security.CustomUserDetails;
import com.ivanfranchin.movieapi.utils.ApplicationConstants;
import com.ivanfranchin.movieapi.utils.ApplicationUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TagRepository tagRepository;

    public MovieServiceImpl() {}

    @Override
    public PagedResponse<Movie> getMovies(int page, int size) {
        ApplicationUtils.validatePageNumberAndSize(page, size);

        // NOTE:
        //! must provide `at least` one property for PagedRequest
        //e.g: `id` || createdAt` or `title`, etc..
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt"); 
        Page<Movie> movies = movieRepository.findAll(pageable);
        List<Movie> content = movies.getNumberOfElements() == 0 ? Collections.emptyList() : movies.getContent();

        return new PagedResponse<>(content, movies.getNumber(), movies.getSize(), 
            movies.getTotalElements(), movies.getTotalPages(), movies.isLast());
    }

    @Override
    public PagedResponse<Movie> getMoviesContainingText(String text, int page, int size) {
        ApplicationUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC);
        Page<Movie> movies = movieRepository.findByImdbContainingOrTitleContainingOrderByTitle(text, text, pageable);
        List<Movie> content = movies.getNumberOfElements() == 0 ? Collections.emptyList() : movies.getContent();

        return new PagedResponse<>(content, movies.getNumber(), movies.getSize(), 
            movies.getTotalElements(), movies.getTotalPages(), movies.isLast());
    }

    @Override
    public Movie validateAndGetMovie(String imdb) {
        return movieRepository.findByImdb(imdb)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "imdb", imdb));
    }

    @Override
    public CreateMovieResponse saveMovie(CreateMovieRequest createMovieRequest, User currentUser) {
        List<String> tagNames = createMovieRequest.getTags();
        List<Tag> tags = new ArrayList<>(tagNames.size());
        for (String name : tagNames) {
            Tag tag = tagRepository.findByName(name).orElse(null);
			tag = tag == null ? tagRepository.save(new Tag(name)) : tag;
            tags.add(tag);
        }

        Movie movie = new Movie();
        movie.setImdb(createMovieRequest.getImdb());
        movie.setTitle(createMovieRequest.getTitle());
        movie.setPoster(createMovieRequest.getPoster());
        movie.setUser(currentUser);
        movie.setTags(tags);

        Movie savedMovie = movieRepository.save(movie);

        CreateMovieResponse response = new CreateMovieResponse();
        response.setImdb(savedMovie.getImdb());
        response.setTitle(savedMovie.getTitle());
        response.setPoster(savedMovie.getPoster());
        response.setTags(savedMovie.getTags().stream().map(tag -> tag.getName()).toList());
        
        return response;
    }

    @Override
    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }

    @Override
    public CreateMovieResponse editMovie(Long id, CreateMovieRequest editMovieRequest, User user) {
        Movie movieToEdit = movieRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Movie", "id", id));
        List<String> tagNames = new ArrayList<>(editMovieRequest.getTags());
        List<Tag> tags = new ArrayList<>();
        for (String name : tagNames) {
            Tag tag = tagRepository.findByName(name).orElse(null);
			tag = tag == null ? tagRepository.save(new Tag(name)) : tag;
            tags.add(tag);
        }
        movieToEdit.setImdb(editMovieRequest.getImdb());
        movieToEdit.setTitle(editMovieRequest.getTitle());
        movieToEdit.setPoster(editMovieRequest.getPoster());
        movieToEdit.setTags(tags);
        movieToEdit.setUser(user);

        Movie savedMovie = movieRepository.save(movieToEdit);

        var response  = new CreateMovieResponse();
        response.setImdb(savedMovie.getImdb());
        response.setTitle(savedMovie.getTitle());
        response.setPoster(savedMovie.getPoster());
        response.setCreatedBy(savedMovie.getCreatedBy());
        response.setUpdatedAt(savedMovie.getUpdatedAt());
        response.setTags(savedMovie.getTags().stream().map(tag -> tag.getName()).toList());

        return response;
    }
}
