package com.example.projekt.dto;
import com.example.projekt.models.Review;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
    @NotNull
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private int year;
    @NotNull
    private String country;
    @NotNull
    private String description;
    @NotNull
    private List<ActorResponse> actors;
    @NotNull
    private GenreResponse genre;
    @NotNull
    private List<ReviewResponse> reviews;

    @Override
    public String toString(){
        return "movie: {" +
                "title: "+ this.title + "\n" +
                "year: "+ this.year + "\n" +
                "description: "+ this.description + "\n" +
                "genre: "+ this.genre + "\n" +
                "reviews: "+ this.reviews + "\n" +
                "actors: "+ this.actors + "\n" +
                "}";
    }
}
