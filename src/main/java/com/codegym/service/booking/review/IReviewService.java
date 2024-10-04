package com.codegym.service.booking.review;

import com.codegym.model.entity.booking.Review;
import com.codegym.service.IGenerateService;

public interface IReviewService extends IGenerateService<Review> {

    Iterable<Review> findReviewByPropertyId(Long propertyId);
}
