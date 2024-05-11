package com.example.exe202backend.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Builder
public class UserModel extends BaseModel{
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String avatar;
    private LocalDate dob;
    private boolean status;

    @OneToMany(mappedBy = "userModel", cascade = CascadeType.ALL)
    private List<UserAddress> addresses;

    @OneToMany(mappedBy = "userModel", cascade = CascadeType.ALL)
    private List<Cart> carts;

    @OneToMany(mappedBy = "userModel", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
