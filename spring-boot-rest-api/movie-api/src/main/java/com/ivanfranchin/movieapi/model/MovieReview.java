package com.ivanfranchin.movieapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ivanfranchin.movieapi.model.audit.UserDateAudit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie_reviews")
public class MovieReview  extends UserDateAudit {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "review_score")
    @DecimalMax("5.0") @DecimalMin("1.0")
    private Double score;

    @Column(name = "text")
    @Size(min = 10, message = "Review text must be minimum 10 characters")
    private String text;
    
    public MovieReview(Double score, String text) {
        this.score = score;
        this.text = text;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    public Movie getMovie() {
        return movie;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }
}
