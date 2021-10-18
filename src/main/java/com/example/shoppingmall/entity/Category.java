package com.example.shoppingmall.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Data
public class Category extends BaseModel{

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

}
