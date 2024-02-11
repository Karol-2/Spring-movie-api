package com.example.projekt.services;

import com.example.projekt.dto.ReviewRequest;
import com.example.projekt.dto.ReviewResponse;
import com.example.projekt.models.Movie;
import com.example.projekt.models.Review;
import com.example.projekt.repositories.ReviewRespository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ReviewService {
    private final ReviewRespository reviewRespository;

    public ReviewService(ReviewRespository reviewRespository) {
        this.reviewRespository = reviewRespository;
    }

    public List<ReviewResponse> getAllReviews(){
        List<Review> Reviews= reviewRespository.findAll();
        return Reviews.stream().map(g -> this.convertToDto(g)).toList();
    }

    public ReviewResponse getReviewById(Long id) throws RuntimeException{
        Review Review = this.reviewRespository.findById(id).orElseThrow(
                ()-> new RuntimeException("Review not found with id: " + id));
        return this.convertToDto(Review);
    }
    public Review getReviewByIdWithoutDto(Long id) throws RuntimeException{
        return this.reviewRespository.findById(id).orElseThrow(
                ()-> new RuntimeException("Review not found with id: " + id));
    }

    public ReviewResponse addReview(Review Review){
        return this.convertToDto(reviewRespository.save(Review));
    }
    public ReviewResponse editReview(Long id, ReviewRequest updatedReview, Movie movie) throws RuntimeException{
        Review existingReview = reviewRespository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("Review not found with id: " + id));

        existingReview.setUsername(updatedReview.getUsername());
        existingReview.setContent(updatedReview.getContent());
        existingReview.setMovie(movie);
        existingReview.setRating(updatedReview.getRating());

        return this.convertToDto(reviewRespository.save(existingReview));
    }

    public void deleteReviewById(Long id){
        reviewRespository.deleteById(id);
    }

    private ReviewResponse convertToDto(Review review){
        ReviewResponse reviewDto = new ReviewResponse();
        BeanUtils.copyProperties(review, reviewDto);
        return reviewDto;
    }
}
