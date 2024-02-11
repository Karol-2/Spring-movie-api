package com.example.projekt.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    @NotNull(message = "Username cannot be null")
    @NotEmpty(message = "Username cannot be empty")
    @Size(max = 25, message = "Username cannot be longer than 25 characters")
    private String username;
    @NotNull(message = "Content cannot be null")
    @NotEmpty(message = "Content cannot be empty")
    @Size(max = 256, message = "Content cannot be longer than 256 characters")
    private String content;
    @NotNull(message = "Rating cannot be null")
    @Digits(integer = 1, fraction = 0, message = "Rating must be a whole number")
    @DecimalMin(value = "1", inclusive = true, message = "Rating must be at least 1")
    @DecimalMax(value = "10", inclusive = true, message = "Rating must be at most 10")
    private Integer rating;
    @NotNull(message = "Movie_id cannot be null")
    private Long movie_id;
}
