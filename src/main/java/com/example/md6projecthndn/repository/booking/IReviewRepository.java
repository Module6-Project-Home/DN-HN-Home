package com.example.md6projecthndn.repository.booking;


import com.example.md6projecthndn.model.dto.review.TotalReviewDTO;
import com.example.md6projecthndn.model.entity.booking.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IReviewRepository extends JpaRepository<Review, Long> {
    Iterable<Review> findReviewByPropertyId(Long propertyId);

    @Query(nativeQuery = true, value = "SELECT COALESCE(COUNT(*), 0)  AS total_reviews, COALESCE(AVG(r.rating), 0.0) AS average_rating FROM reviews r WHERE r.property_id = :propertyId;")
    Object getTotalReviewsByPropertyId(Long propertyId);
}
