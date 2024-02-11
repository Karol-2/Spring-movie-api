package com.example.projekt.repositories;

import com.example.projekt.models.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    @Query("SELECT DISTINCT a FROM Actor a JOIN a.movies m JOIN m.genre g WHERE g.name = :genreName")
    List<Actor> findActorsByGenre(@Param("genreName") String genreName);

    @Query("SELECT DISTINCT a FROM Actor a JOIN a.movies m WHERE m.year = :year")
    List<Actor> findActorsByMovieYear(@Param("year") int year);
}
