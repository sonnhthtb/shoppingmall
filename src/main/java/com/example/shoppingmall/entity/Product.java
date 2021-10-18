package com.example.shoppingmall.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
public class Product extends BaseModel{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "unit_price", nullable = false)
    private float unitPrice;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "unit_in_stock", nullable = false)
    private int unitInStock;

    @Column(name = "content", nullable = false)
    @Lob
    private String content;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "product_category",
                joinColumns = @JoinColumn(name = "product_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

}
