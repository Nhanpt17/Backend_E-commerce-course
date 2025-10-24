package com.human.graduateproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long OrderItemId;
    private Long id;
    private String name;
    private Double price;
    private Long categoryId;
    private String categoryName;
    private Integer quantity;


    // Constructors, getters, setters
}
