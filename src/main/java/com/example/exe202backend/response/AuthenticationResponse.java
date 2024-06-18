package com.example.exe202backend.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String access_token;
    private String refresh_token;
}
