package com.example.md6projecthndn.model.dto.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private int rating;
    private String comment;
    private String guest;
    private String avatar;
    private LocalDateTime createdAt;
}
