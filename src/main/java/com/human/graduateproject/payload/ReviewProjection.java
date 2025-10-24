package com.human.graduateproject.payload;


public interface ReviewProjection {
    Long getId();
    String getComment();
    Integer getRating();
    String getProductName();
    String getUsername();
}
