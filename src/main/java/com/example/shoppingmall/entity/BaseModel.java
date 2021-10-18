package com.example.shoppingmall.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseModel implements Serializable {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "id", nullable = false, insertable = false, updatable = false)
      @EqualsAndHashCode.Include
      protected Long id;

      @Column(name = "created_at")
      @CreatedDate
      protected Timestamp createdAt;

      @Column(name = "updated_at")
      @LastModifiedDate
      protected Timestamp updatedAt;

      @Column(name = "is_deleted")
      protected Boolean isDeleted = false;

}
