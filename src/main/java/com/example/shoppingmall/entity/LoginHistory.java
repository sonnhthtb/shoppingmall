package com.example.shoppingmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "login_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginHistory extends BaseModel{

    @Column(name = "login_at")
    private Timestamp loginAt;

}
