package com.human.graduateproject.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;

    private String name;

    private Double price;

    private Long stock; // ton kho

    private String description;


//    private byte[] byteImg;
    private String imgUrl;  // thêm dòng này

    private Long categoryId;


    //private MultipartFile img;
}
