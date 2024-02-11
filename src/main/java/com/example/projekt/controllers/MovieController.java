package com.example.projekt.controllers;

import com.example.projekt.dto.Message;
import com.example.projekt.dto.MovieRequest;
import com.example.projekt.dto.MovieResponse;
import com.example.projekt.dto.MovieScoreDto;
import com.example.projekt.models.Actor;
import com.example.projekt.models.Genre;
import com.example.projekt.models.Movie;
import com.example.projekt.repositories.MovieRepository;
import com.example.projekt.services.ActorService;
import com.example.projekt.services.GenreService;
import com.example.projekt.services.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    private final ActorService actorService;
    private final GenreService genreService;
    private final MovieRepository movieRepository;

    public MovieController(MovieService movieService, ActorService actorService, GenreService genreService, MovieRepository movieRepository) {
        this.movieService = movieService;
        this.actorService = actorService;
        this.genreService = genreService;
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public List<MovieResponse> getAll(){
        List<Movie> movies = movieService.getAllMovies();

        return movies.stream().map(m->movieService.convertToDto(m)).toList();
    }

    @GetMapping("/with-min-reviews/{number}")
    public ResponseEntity<?> findMoviesWithMinReviews(@PathVariable int number){
        if (number > 0){
            List<Movie> moviesFound =movieService.findMoviesWithMinReviews(number);
            List<MovieResponse> moviesConverted =moviesFound.stream().map(m->movieService.convertToDto(m)).toList();
            return new ResponseEntity<>(moviesConverted,HttpStatus.OK);
        }

        return new ResponseEntity<>(new Message("Number must be a number, at least 1"),HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<MovieScoreDto>> findMoviesWithRating(){
            List<MovieScoreDto> moviesFound =movieService.getMoviesWithAverageRating();
            return new ResponseEntity<>(moviesFound,HttpStatus.OK);

    }

    @GetMapping("/actor/{id}")
    public ResponseEntity<?> findMoviesWithMinReviews(@PathVariable Long id){
        try{
            List<MovieResponse> movies = movieService.getMoviesByActorId(id);
            return new ResponseEntity<>(movies,HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(new Message(e.getMessage()),HttpStatus.BAD_REQUEST);
        }


    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        try{
            Movie movie = movieService.getMovieById(id);
            MovieResponse movieResponse = movieService.convertToDto(movie);

            return new ResponseEntity<>(movieResponse, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping
    public ResponseEntity<?> createMovie(@Valid @RequestBody MovieRequest newMovie) {
        try {
            Genre genre = genreService.getGenreByIdWithoutDto(newMovie.getGenreId());
            List<Actor> actors = new ArrayList<>();

            for (Long actorId : newMovie.getActorsIds()) {
                Actor actor = actorService.getActorByIdWithoutDto(actorId);
                actors.add(actor);
            }

            Movie movie = new Movie(newMovie.getTitle(), newMovie.getYear(),
                    newMovie.getCountry(), newMovie.getDescription(), genre);

            for (Actor actor : actors) {
                movie.addActor(actor);
            }

            Movie savedMovie = movieService.addMovie(movie);

            return new ResponseEntity<>(movieService.convertToDto(savedMovie), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest updatedMovie){

        try{
            Genre genre = genreService.getGenreByIdWithoutDto(updatedMovie.getGenreId());
            List<Actor> actors = new ArrayList<>();


            for (Long actorId: updatedMovie.getActorsIds()) {
                Actor actor = actorService.getActorByIdWithoutDto(actorId);
                actors.add(actor);
            }
            Movie newMovie = movieService.editMovie(id, updatedMovie,genre,actors);

            MovieResponse movieresp =  movieService.convertToDto(newMovie);

            return new ResponseEntity<>(movieresp, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Message> deleteMovie(@PathVariable Long id){
        try{
            movieService.getMovieById(id);
            movieService.deleteMovieById(id);
            return ResponseEntity.ok(new Message("Success!"));
        } catch (RuntimeException e){
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HashMap<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
        HashMap<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error)-> {
            String fieldname = ((FieldError) error).getField();
            String errorMsg = error.getDefaultMessage();
            errors.put(fieldname, errorMsg);
        });
        return errors;
    }
}
