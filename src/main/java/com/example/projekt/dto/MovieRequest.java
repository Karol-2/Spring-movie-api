package com.example.projekt.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {
    @NotNull(message = "Title cannot be null")
    @NotEmpty(message = "Title cannot be empty")
    @Size(max = 100, message = "Title cannot be longer than 100 characters")
    private String title;
    @NotNull(message = "Year cannot be null")
    @Min(value = 1900,message = "Year cannot be at least 1900")
    @Max(value = 2024,message = "Year must be at most 2024")
    private int year;
    @NotNull(message = "Country cannot be null")
    @NotEmpty(message = "Country cannot be empty")
    @Size(max = 3,min = 3, message = "Title must be in a 3 letter format")
    private String country;
    @NotNull(message = "Description cannot be null")
    @NotEmpty(message = "Description cannot be empty")
    @Size(max = 256, message = "Description cannot be longer than 256 characters")
    private String description;
    @NotNull(message = "ActorsId cannot be null")
    @Size(min = 1, message = "At least one actorId must be present")
    private List<Long> actorsIds;
    @NotNull(message = "GenreId cannot be null")
    private Long genreId;

}
