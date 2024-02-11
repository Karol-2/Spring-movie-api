package com.example.projekt.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MovieScoreDto {
    private Long id;
    private String title;
    private Double score;

    public MovieScoreDto(Long id, String title, Double score) {
        this.id = id;
        this.title = title;
        this.score = score;
    }
    public MovieScoreDto(){};
}
