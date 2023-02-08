package com.ivanfranchin.movieapi.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ivanfranchin.movieapi.model.audit.UserDateAudit;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "movies", uniqueConstraints = { @UniqueConstraint(columnNames = { "imdb" }) })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Movie extends UserDateAudit {

    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "imdb", nullable = false)
    private String imdb;

    @Column(nullable = false, length = 100)
    private String title;

    private String poster;

    @ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "movie_tag", joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
	private List<Tag> tags = new ArrayList<>();

    @JsonIgnore
	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MovieReview> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

    public Movie() {}

    public Movie(String imdb, String title, String poster) {
        this.imdb = imdb;
        this.title = title;
        this.poster = poster;
    }

    public List<Tag> getTags() {
		return tags == null ? null : new ArrayList<>(tags);
	}

	public void setTags(List<Tag> tags) {
        if (tags != null) 
            this.tags.clear();
        this.tags.addAll(tags);
	}

    // utility methods that enable bidirectional associations
    // reference: https://vladmihalcea.com/jpa-hibernate-synchronize-bidirectional-entity-associations/
    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getMovies().add(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getMovies().remove(this);
    }

    public List<MovieReview> getReviews() {
        return reviews == null ? null : new ArrayList<>(reviews);
	}

	public void setReviews(List<MovieReview> reviews) {
		if (reviews == null) 
            this.reviews = null;
        else 
			this.reviews = Collections.unmodifiableList(reviews);
	}

    @JsonIgnore
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
