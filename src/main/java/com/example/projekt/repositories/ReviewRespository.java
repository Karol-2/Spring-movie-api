package com.example.projekt.repositories;

import com.example.projekt.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRespository extends JpaRepository<Review, Long> {

}
