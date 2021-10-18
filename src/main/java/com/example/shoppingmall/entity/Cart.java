package com.example.shoppingmall.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "cart")
public class Cart extends BaseModel{

    @Column(name = "sku")
    private String sku;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Set<CartItem> cartItem;


}
