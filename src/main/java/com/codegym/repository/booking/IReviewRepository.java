package com.codegym.repository.booking;

import com.codegym.model.entity.booking.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReviewRepository extends JpaRepository<Review, Long> {
    Iterable<Review> findReviewByPropertyId(Long propertyId);
}
