package com.example.md6projecthndn.repository.booking;


import com.example.md6projecthndn.model.entity.booking.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReviewRepository extends JpaRepository<Review, Long> {
    Iterable<Review> findReviewByPropertyId(Long propertyId);
}
