package com.example.exe202backend.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {
    private long id;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private LocalDate dob;
    private boolean status;
}
