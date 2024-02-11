package com.example.projekt.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    @NotNull
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String content;
    @NotNull
    private Integer rating;
    @NotNull
    private LocalDateTime dateAdded;
}
