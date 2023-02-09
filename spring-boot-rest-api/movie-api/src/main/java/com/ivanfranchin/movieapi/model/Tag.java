package com.ivanfranchin.movieapi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ivanfranchin.movieapi.model.audit.DateAudit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "tags", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Tag extends DateAudit  {

    private static final long serialVersionUID = -5298707266367331514L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(nullable = true, length = 200)
    private String description;

    @JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "movie_tag", joinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"))
	private List<Movie> movies;

    public Tag(String name) {
        super();
        this.name = name;
    }

    public Tag(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public List<Movie> getMovies() {
        return movies == null ? null : new ArrayList<Movie>();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies == null ? null : Collections.unmodifiableList(movies);
    }
}