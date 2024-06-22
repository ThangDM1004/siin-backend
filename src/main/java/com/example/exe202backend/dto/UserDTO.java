package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Builder
public class UserDTO {
    private int id;
    private String email;
    private String fullName;
    private String password;
    private String phone;
    private String avatar;
    private LocalDate dob;
}
