package com.codegym.service.booking.review;

import com.codegym.model.entity.booking.Review;
import com.codegym.repository.booking.IReviewRepository;
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
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
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
