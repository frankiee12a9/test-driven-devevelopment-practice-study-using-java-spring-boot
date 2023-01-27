package com.ivanfranchin.movieapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ivanfranchin.movieapi.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>  {
    
    @Query("SELECT t FROM Tag t where t.name in :names")
    List<Tag> findAllInName(List<String> names);

    Optional<Tag> findByName(String name);
}
