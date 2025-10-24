package com.human.graduateproject.payload;

import lombok.Data;

@Data
public class ReviewRequeset {

    private Long productId;
    private Long userId;
    private Integer rating; // 1-5
    private String comment;
}
