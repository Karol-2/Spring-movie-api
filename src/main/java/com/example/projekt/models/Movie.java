package com.example.projekt.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int year;
    private String country;
    private String description;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Review> reviews;

    @ManyToMany(mappedBy = "movies", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Actor> actors;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public Movie(String title, int year, String country,String description, Genre genre) {
        this.title = title;
        this.year = year;
        this.country = country;
        this.description = description;
        this.genre = genre;
        this.reviews = new ArrayList<>();
        this.actors = new ArrayList<>();
    }

    public Movie() {
        this.reviews = new ArrayList<>();
        this.actors = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public void addActor(Actor actor) {
        if (actors == null) {
            actors = new ArrayList<>();
        }
        actors.add(actor);
        actor.getMovies().add(this);
    }

    public void removeActor(Actor actor) {
        if (actors != null) {
            actors.remove(actor);
            actor.getMovies().remove(this);
        }
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
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
