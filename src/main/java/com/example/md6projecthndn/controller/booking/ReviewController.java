package com.example.md6projecthndn.controller.booking;

import com.example.md6projecthndn.model.dto.review.TotalReviewDTO;
import com.example.md6projecthndn.service.booking.review.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private IReviewService reviewService;

    @GetMapping("/total")
    public ResponseEntity<?> getTotalReview(@RequestParam Long propertyId) {
        TotalReviewDTO totalReviewDTO = reviewService.getTotalReviewsByPropertyId(propertyId);

        if (totalReviewDTO != null) {
            return ResponseEntity.ok(totalReviewDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ngôi nhà chưa có đánh giá");
        }
    }

}
