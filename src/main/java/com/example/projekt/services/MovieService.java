package com.example.projekt.services;

import com.example.projekt.dto.*;
import com.example.projekt.models.Actor;
import com.example.projekt.models.Genre;
import com.example.projekt.models.Movie;
import com.example.projekt.models.Review;
import com.example.projekt.repositories.ActorRepository;
import com.example.projekt.repositories.GenreRepository;
import com.example.projekt.repositories.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovieService {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;

    public MovieService(MovieRepository movieRepository, ActorRepository actorRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.genreRepository = genreRepository;
    }

    public List<Movie> getAllMovies(){
        List<Movie> movies=movieRepository.findAll();
        return movies;
    }

    public Movie getMovieById(Long id) throws RuntimeException{
        Movie movie= this.movieRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Movie not found with id: " + id));

        return movie;
    }
    public Movie getMovieByIdWithoutDto(Long id) throws  RuntimeException{
        return this.movieRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Movie not found with id: " + id));
    }
    public List<MovieResponse> getMoviesByActorId(Long id) throws RuntimeException{
        Actor actor =actorRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Actor not found with id: " + id));

        List<Movie> movies = actor.getMovies();
        return this.convertListToDto(movies);
    }
    public List<MovieScoreDto> getMoviesWithAverageRating(){
        List<Object[]> movies = this.movieRepository.findMoviesWithAverageRating();
        return movies.stream()
                .map(el -> new MovieScoreDto((Long) el[1], (String) el[0], (Double) el[2]))
                .sorted((m1, m2) -> m2.getScore().compareTo(m1.getScore()))
                .toList();
    }
    public Movie addMovie(Movie Movie){
        return movieRepository.save(Movie);
    }
    public Movie editMovie(Long id, MovieRequest updatedMovie, Genre genre, List<Actor> actors) throws RuntimeException{
        Movie existingMovie = movieRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("Movie not found with id: " + id));

        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setCountry(updatedMovie.getCountry());
        existingMovie.setYear(updatedMovie.getYear());
        existingMovie.setDescription(updatedMovie.getDescription());
        existingMovie.setGenre(genre);

        List<Actor> existingActors = existingMovie.getActors();

        for (int i = 0; existingActors.size()!=0; i++) {
            Actor actor = existingActors.get(0);
            actor.removeMovie(existingMovie);
        }


        for (Actor actor : actors) {
            existingMovie.addActor(actor);
//            actor.addMovie(existingMovie);
        }


        return movieRepository.save(existingMovie);
    }

    public void deleteMovieById(Long id) throws RuntimeException{

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        List<Actor> movieActors = movie.getActors();

        for (int i = 0; movieActors.size()!=0; i++) {
            Actor actor = movieActors.get(0);
            actor.removeMovie(movie);
        }

        movieRepository.deleteById(id);
    }
    public List<Movie> findMoviesWithMinReviews(int minReviews){
        return movieRepository.findMoviesWithMinReviews(minReviews);
    }

    private List<MovieResponse> convertListToDto(List<Movie> movies){
        return movies.stream().map(movie -> this.convertToDto(movie)).toList();
    }

    public MovieResponse convertToDto(Movie movie){

        Genre genre = movie.getGenre();
        List<Actor> actors = movie.getActors();
        List<Review> reviews = movie.getReviews();

        GenreResponse genreDto = new GenreResponse();
        BeanUtils.copyProperties(genre, genreDto);

        List<ActorResponse> actorsDtos = new ArrayList<>();
        for (Actor actor:actors ) {
            ActorResponse actorDto = new ActorResponse();
            BeanUtils.copyProperties(actor, actorDto);
            actorsDtos.add(actorDto);
        }

        List<ReviewResponse> reviewsDtos = new ArrayList<>();
        if ( reviews != null && reviews.size() > 0){
            for (Review review:reviews ) {
                ReviewResponse reviewDto = new ReviewResponse();
                BeanUtils.copyProperties(review, reviewDto);
                reviewsDtos.add(reviewDto);
            }
        }


        MovieResponse movieDto = new MovieResponse();
        BeanUtils.copyProperties(movie, movieDto);

        movieDto.setActors(actorsDtos);
        movieDto.setReviews(reviewsDtos);
        movieDto.setGenre(genreDto);

        return movieDto;
    }
}
