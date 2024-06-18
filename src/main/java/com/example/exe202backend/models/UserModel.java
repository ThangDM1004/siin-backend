package com.example.exe202backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@SuperBuilder
public class UserModel extends BaseModel implements UserDetails {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String avatar;
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "users")
    @JsonIgnore
    private Set<Token> tokens;

    @OneToMany(mappedBy = "userModel", cascade = CascadeType.ALL)
    private List<UserAddress> addresses;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "userModel", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
