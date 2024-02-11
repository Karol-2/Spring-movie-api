package com.example.projekt.controllers;

import com.example.projekt.dto.ReviewRequest;
import com.example.projekt.dto.ReviewResponse;
import com.example.projekt.dto.Message;
import com.example.projekt.models.Movie;
import com.example.projekt.models.Review;
import com.example.projekt.services.MovieService;
import com.example.projekt.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final MovieService movieService;

    public ReviewController(ReviewService reviewService, MovieService movieService) {
        this.reviewService = reviewService;
        this.movieService = movieService;
    }

    @GetMapping
    public List<ReviewResponse> getAll(){
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        try{
            ReviewResponse Review = reviewService.getReviewById(id);
            return new ResponseEntity<>(Review, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewRequest newReview){
        try{
            Movie movie = movieService.getMovieByIdWithoutDto(newReview.getMovie_id());

            Review review = new Review();
            review.setRating(newReview.getRating());
            review.setMovie(movie);
            review.setContent(newReview.getContent());
            review.setUsername(newReview.getUsername());

            return new ResponseEntity<>(reviewService.addReview(review),HttpStatus.CREATED);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(new Message(e.getMessage()),HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest updatedReview){
        try{
            Movie movie = movieService.getMovieByIdWithoutDto(updatedReview.getMovie_id());
            ReviewResponse newReview = reviewService.editReview(id, updatedReview, movie);

            return new ResponseEntity<>(newReview, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id){
        try{
            reviewService.getReviewById(id);
            reviewService.deleteReviewById(id);
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
