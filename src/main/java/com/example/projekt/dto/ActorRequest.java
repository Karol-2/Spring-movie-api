package com.example.projekt.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorRequest {
    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 25, message = "Name cannot be longer than 25 characters")
    private String name;
    @NotNull(message = "Surname cannot be null")
    @NotEmpty(message = "Surname cannot be empty")
    @Size(max = 75, message = "Surname cannot be longer than 75 characters")
    private String surname;
    @NotNull(message = "Age cannot be null")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120,message = "Age must be at most 120")
    private int age;

}
