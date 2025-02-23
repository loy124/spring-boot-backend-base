package com.loy124.myapp.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;


@Data
public class LoginRequestDto {


    @NotBlank
    String email;

    @NotBlank
    String password;

    String domainName;

    public LoginRequestDto(String email, String password, String domainName) {
        this.email = email;
        this.password = password;
        this.domainName = domainName;
    }
}