package com.example.projekt.services;

import com.example.projekt.dto.ActorRequest;
import com.example.projekt.dto.ActorResponse;
import com.example.projekt.models.Actor;
import com.example.projekt.repositories.ActorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ActorService {
    
    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<ActorResponse> getAllActors(){
        List<Actor> actors= actorRepository.findAll();
        return actors.stream().map(a -> this.convertToDto(a)).toList();
    }

    public ActorResponse getActorById(Long id) throws RuntimeException{
        Actor actor = this.actorRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Actor not found with id: " + id));
        return this.convertToDto(actor);
    }
    public List<ActorResponse> getActorsByMovieGenre(String genreName){
        List<Actor> actors = actorRepository.findActorsByGenre(genreName);
        return actors.stream().map(a -> this.convertToDto(a)).toList();
    }
    public List<ActorResponse> getActorsByMovieYear(int year){
        List<Actor> actors = actorRepository.findActorsByMovieYear(year);
        return actors.stream().map(a -> this.convertToDto(a)).toList();
    }

    public Actor getActorByIdWithoutDto(Long id) throws  RuntimeException{
        return this.actorRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Actor not found with id: " + id));
    }
    public ActorResponse addActor(Actor Actor){
        return this.convertToDto(actorRepository.save(Actor));
    }
    public ActorResponse editActor(Long id, ActorRequest updatedActor) throws RuntimeException{
        Actor existingActor = actorRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("Actor not found with id: " + id));

        existingActor.setName(updatedActor.getName());
        existingActor.setSurname(updatedActor.getSurname());
        existingActor.setAge(updatedActor.getAge());

        return this.convertToDto(actorRepository.save(existingActor));
    }

    public void deleteActorById(Long id){
        actorRepository.deleteById(id);
    }

    private ActorResponse convertToDto(Actor actor){
        ActorResponse actorDto = new ActorResponse();
        BeanUtils.copyProperties(actor, actorDto);
        return actorDto;
    }
    
}
