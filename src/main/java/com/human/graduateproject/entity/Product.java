package com.human.graduateproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("enable=true")
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long stock = 0L; // ton kho

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean enable = true; // soft delete

    @Lob //luc dung mysql thi bo lai
    //@Column(columnDefinition = "TEXT")// qua ben mysql thi bo cai nay di
    private String description;

//    @Lob
//    @Column(columnDefinition = "longblob")
//    private byte[] img;
    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "category_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;




    public Product(Long id,String name,   Double price, String description, String imgUrl,Category category) {
    this.id = id;
    this.category = category;
    this.name = name;
    this.price = price;
    this.description = description;
    this.imgUrl = imgUrl;
}


}
