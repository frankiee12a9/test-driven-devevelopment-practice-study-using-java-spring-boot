package com.ivanfranchin.movieapi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ivanfranchin.movieapi.security.oauth2.OAuth2Provider;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    @JsonIgnore
    private String password;
    private String name;
    private String email;
    private String role;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    private String providerId;

    @JsonIgnore
	@OneToMany(
        mappedBy = "user", orphanRemoval = true
    )
	private List<MovieReview> movieReviews = new ArrayList<>();

    @JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Movie> movies = new ArrayList<>();

    public User(String username, String password, String name, String email, String role, String imageUrl, OAuth2Provider provider, String providerId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.imageUrl = imageUrl;
        this.provider = provider;
        this.providerId = providerId;
    }

    public List<MovieReview> getMovieReviews() {
		return movieReviews == null ? null : new ArrayList<>(movieReviews);
	}

	public void setMovieReviews(List<MovieReview> movieReviews) {
        if (movieReviews != null) 
            movieReviews.clear();
        movieReviews.addAll(movieReviews);
		// if (movieReviews == null) 
		// 	this.movieReviews = null;
        // else 
		// 	this.movieReviews = Collections.unmodifiableList(movieReviews);
	}

    public List<Movie> getMovies() {
		return movies == null ? null : new ArrayList<>(movies);
	}

	public void setMovies(List<Movie> movies) {
        if (movies != null)
            movies.clear();
        movies.addAll(movies);
		// if (movies == null) 
		// 	this.movies = null;
        // else 
		// 	this.movies = Collections.unmodifiableList(movies);
	}
}
