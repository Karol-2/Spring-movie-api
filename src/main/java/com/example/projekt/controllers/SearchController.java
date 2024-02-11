package com.example.projekt.controllers;

import com.example.projekt.dto.GenreResponse;
import com.example.projekt.dto.MovieResponse;
import com.example.projekt.models.Movie;
import com.example.projekt.repositories.MovieRepository;
import com.example.projekt.services.GenreService;
import com.example.projekt.services.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final MovieRepository movieRepository;

    public SearchController(MovieService movieService, GenreService genreService, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.genreService = genreService;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(
            @RequestParam Map<String, String> query){

        String title = query.get("title");
        Integer year = parseInteger(query.get("year"));
        String country = query.get("country");
        String genreName = query.get("genre");


        try {
            GenreResponse genre = this.genreService.getGenreByName(genreName);
            genreName = genre.getName();
        } catch (RuntimeException ignored){
        }


        if(title == null && year == null && country == null && genreName == null){
            List<Movie> movies = movieRepository.findAll();
            return ResponseEntity.ok(convertToDtoList(movies));
        }

        System.out.println("title "+ title);
        System.out.println("year "+ year);
        System.out.println("country "+ country);
        System.out.println("genreName "+ genreName);

        List<Movie> movies = movieRepository.findMoviesByCriteria(title, year, country, genreName);
        return ResponseEntity.ok(convertToDtoList(movies));
    }

    private Integer parseInteger(String value) {
        try {
            return value != null ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long parseLong(String value) {
        try {
            return value != null ? Long.parseLong(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private List<MovieResponse> convertToDtoList(List<Movie> movies) {
        return movies.stream()
                .map(movieService::convertToDto)
                .toList();
    }
}
