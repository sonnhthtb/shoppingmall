package com.example.shoppingmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "verify_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyToken extends BaseModel{

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    private Timestamp expiryDate;

}
