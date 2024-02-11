package com.example.projekt.repositories;

import com.example.projekt.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m JOIN m.reviews r GROUP BY m HAVING COUNT(r) >= :minReviews")
    List<Movie> findMoviesWithMinReviews(@Param("minReviews") int minReviews);

    @Query("SELECT m.title, m.id, COALESCE(AVG(r.rating), 0) as score FROM Movie m LEFT JOIN m.reviews r GROUP BY m.id, m.title")
    List<Object[]> findMoviesWithAverageRating();

    @Query("SELECT m FROM Movie m " +
            "JOIN m.genre g " +
            "WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:year IS NULL OR m.year = :year) " +
            "AND (:country IS NULL OR m.country LIKE CONCAT('%', :country, '%')) " +
            "AND (:genreName IS NULL OR g.name LIKE CONCAT('%', :genreName, '%'))")
    List<Movie> findMoviesByCriteria(
            @Param("title") String title,
            @Param("year") Integer year,
            @Param("country") String country,
            @Param("genreName") String genreName
    );

}

