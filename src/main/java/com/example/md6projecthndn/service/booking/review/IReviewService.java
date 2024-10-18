package com.example.md6projecthndn.service.booking.review;


import com.example.md6projecthndn.model.dto.review.TotalReviewDTO;
import com.example.md6projecthndn.model.entity.booking.Review;
import com.example.md6projecthndn.service.IGenerateService;

public interface IReviewService extends IGenerateService<Review> {

    Iterable<Review> findReviewByPropertyId(Long propertyId);

    TotalReviewDTO getTotalReviewsByPropertyId(Long propertyId);
}
