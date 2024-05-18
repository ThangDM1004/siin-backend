package com.example.exe202backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@MappedSuperclass
@Data
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @LastModifiedBy
    @JsonIgnore
    private String modifiedBy;

    @Column
    @CreatedBy
    @JsonIgnore
    private String createBy;

    @Column
    @LastModifiedDate
    @JsonIgnore
    private LocalDate modifiedDate;

    @Column
    @CreatedDate
    private LocalDate createDate;
    @Column
    private boolean status = true;
}
