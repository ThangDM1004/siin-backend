package com.example.exe202backend.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@SuperBuilder
public class UserModel extends BaseModel{
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String avatar;
    private LocalDate dob;

    @OneToMany(mappedBy = "userModel", cascade = CascadeType.ALL)
    private List<UserAddress> addresses;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "userModel", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
