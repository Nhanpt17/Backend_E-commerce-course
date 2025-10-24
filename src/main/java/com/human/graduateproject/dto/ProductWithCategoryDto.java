package com.human.graduateproject.dto;

public interface ProductWithCategoryDto {
    Long getId();
    String getName();
    Double getPrice();
    Long getStock();
    String getDescription();
    String getImgUrl();
    Long getCategoryId();
    String getCategoryName();
}
