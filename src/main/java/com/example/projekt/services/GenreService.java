package com.example.projekt.services;

import com.example.projekt.dto.GenreRequest;
import com.example.projekt.dto.GenreResponse;
import com.example.projekt.models.Genre;
import com.example.projekt.repositories.GenreRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreResponse> getAllGenres(){
       List<Genre> genres= genreRepository.findAll();
       return genres.stream().map(g -> this.convertToDto(g)).toList();
    }

    public GenreResponse getGenreById(Long id) throws RuntimeException{
        Genre genre = this.genreRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Genre not found with id: " + id));
        return this.convertToDto(genre);
    }
    public GenreResponse getGenreByName(String name) throws RuntimeException{
        Genre genre = this.genreRepository.findByName(name).orElseThrow(
                ()-> new RuntimeException("Genre not found with name: " + name));
        return this.convertToDto(genre);
    }
    public Genre getGenreByIdWithoutDto(Long id) throws RuntimeException{
        return this.genreRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Genre not found with id: " + id));
    }

    public GenreResponse addGenre(Genre genre){
        return this.convertToDto(genreRepository.save(genre));
    }
    public GenreResponse editGenre(Long id, GenreRequest updatedGenre) throws RuntimeException{
        Genre existingGenre = genreRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("Genre not found with id: " + id));

        existingGenre.setName(updatedGenre.getName());
        return this.convertToDto(genreRepository.save(existingGenre));
    }

    public void deleteGenreById(Long id){
        genreRepository.deleteById(id);
    }

    private GenreResponse convertToDto(Genre genre){
        GenreResponse genreDto = new GenreResponse();
            BeanUtils.copyProperties(genre, genreDto);
            return genreDto;
    }

}
