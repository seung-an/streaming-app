package com.example.streamingapp.dto;

import lombok.Data;

@Data
public class MemberLoginRequestDto {
    private String memberId;
    private String password;
}
