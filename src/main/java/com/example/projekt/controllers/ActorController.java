package com.example.projekt.controllers;

import com.example.projekt.dto.ActorRequest;
import com.example.projekt.dto.ActorResponse;
import com.example.projekt.dto.Message;
import com.example.projekt.models.Actor;
import com.example.projekt.services.ActorService;
import com.example.projekt.services.GenreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {
    private final ActorService actorService;
    private final GenreService genreService;

    public ActorController(ActorService actorService, GenreService genreService) {
        this.actorService = actorService;
        this.genreService = genreService;
    }

    @GetMapping
    public List<ActorResponse> getAll(){
        return actorService.getAllActors();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        try{
            ActorResponse Actor = actorService.getActorById(id);
            return new ResponseEntity<>(Actor, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/by-genre/{genreName}")
    public ResponseEntity<?> getActorsByMovieGenre(@PathVariable String genreName){
        try{
            genreService.getGenreByName(genreName);
            List<ActorResponse> actors= actorService.getActorsByMovieGenre(genreName);
            return ResponseEntity.ok(actors);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(new Message(e.getMessage()),HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/by-movie-year/{yearVar}")
    public ResponseEntity<?> getActorsByMovieYear(@PathVariable String yearVar) {
        try {
            int year = Integer.parseInt(yearVar);

            if (isValidYear(year)) {
                List<ActorResponse> actor =actorService.getActorsByMovieYear(year);
                return ResponseEntity.ok(actor);

            } else {
                return new ResponseEntity<>(new Message("Year must be between 1900 and 2024"),HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(new Message("Invalid year format"),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping
    public ResponseEntity<ActorResponse> createActor(@Valid @RequestBody ActorRequest newActor){
        Actor Actor = new Actor(newActor.getName(),newActor.getSurname(),newActor.getAge());
        return new ResponseEntity<>(actorService.addActor(Actor),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editActor(@PathVariable Long id, @Valid @RequestBody ActorRequest updatedActor){
        try{
            ActorResponse newActor = actorService.editActor(id, updatedActor);
            return new ResponseEntity<>(newActor, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Message> deleteActor(@PathVariable Long id){
        try{
            this.actorService.getActorById(id);
            this.actorService.deleteActorById(id);
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

    private boolean isValidYear(int year) {
        return year >= 1900 && year <= 2024;
    }
}
