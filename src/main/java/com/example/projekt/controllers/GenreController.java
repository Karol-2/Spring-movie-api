package com.example.projekt.controllers;

import com.example.projekt.dto.ActorResponse;
import com.example.projekt.dto.Message;
import com.example.projekt.dto.GenreRequest;
import com.example.projekt.dto.GenreResponse;
import com.example.projekt.models.Genre;
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
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<GenreResponse> getAll(){
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        try{
            GenreResponse genre = genreService.getGenreById(id);
            return new ResponseEntity<>(genre, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping
    public ResponseEntity<?> createGenre(@Valid @RequestBody GenreRequest newGenre){
        try{
            String existingGenreName = genreService.getGenreByName(newGenre.getName()).getName();
            // exists
            return new ResponseEntity<>(new Message("Genre with this name already exists!"),HttpStatus.CONFLICT);
        } catch (RuntimeException e){
            // doesnt exists
            Genre genre = new Genre(newGenre.getName());
            GenreResponse genreResponse = genreService.addGenre(genre);
            return new ResponseEntity<>(genreResponse,HttpStatus.CREATED);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editGenre(@PathVariable Long id, @Valid @RequestBody GenreRequest updatedGenre){
        try{
            GenreResponse newGenre = genreService.editGenre(id, updatedGenre);
            return new ResponseEntity<>(newGenre, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long id){
        try{
            genreService.getGenreById(id);
            genreService.deleteGenreById(id);
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
