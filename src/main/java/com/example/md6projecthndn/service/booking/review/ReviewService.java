package com.example.md6projecthndn.service.booking.review;


import com.example.md6projecthndn.model.dto.review.TotalReviewDTO;
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

    @Override
    public TotalReviewDTO getTotalReviewsByPropertyId(Long propertyId) {

       Object object = reviewRepository.getTotalReviewsByPropertyId(propertyId);
        if(object != null){
       Object[] fields = (Object[]) object;
       TotalReviewDTO dto = new TotalReviewDTO(
               ((Number) fields[0]).intValue(),
               ((Number) fields[1]).doubleValue()
       );
       return dto;
    }
        return null;
    }
}
