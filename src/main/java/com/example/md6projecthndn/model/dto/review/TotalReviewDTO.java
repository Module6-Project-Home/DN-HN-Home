package com.example.md6projecthndn.model.dto.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TotalReviewDTO {
    private int total;
    private double reviewed;

    public TotalReviewDTO(int total, double reviewed) {
        this.total = total;
        this.reviewed = reviewed;
    }
}
