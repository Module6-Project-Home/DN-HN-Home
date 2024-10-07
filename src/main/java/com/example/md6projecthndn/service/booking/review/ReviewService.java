package com.example.md6projecthndn.service.booking.review;


import com.example.md6projecthndn.model.entity.booking.Review;
import com.example.md6projecthndn.repository.booking.IReviewRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService implements IReviewService {
    private final IReviewRepository reviewRepository;

    public ReviewService(IReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Iterable<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public void save(Review review) {
    reviewRepository.save(review);
    }

    @Override
    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
    reviewRepository.deleteById(id);
    }

    @Override
    public Iterable<Review> findReviewByPropertyId(Long propertyId) {
        return reviewRepository.findReviewByPropertyId(propertyId);
    }
}
