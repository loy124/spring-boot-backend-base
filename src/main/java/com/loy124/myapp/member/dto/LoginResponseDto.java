package com.loy124.myapp.member.dto;

import lombok.Value;
import com.loy124.myapp.member.entity.Member;

/**
 * DTO for {@link Member}
 */
@Value
public class LoginResponseDto {
    Long id;
    String email;
    String role;
    String phoneNumber;
    String goal;
    String name;
    String employeeNumber;
    String language;

    String affiliatedCode;


    public LoginResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.role = "ROLE_" +member.getRole();
        this.phoneNumber = member.getPhoneNumber();
        this.goal = member.getGoal();
        this.name = member.getName();
        this.employeeNumber = member.getPhoneNumber();
        this.language = member.getLanguage();
        this.affiliatedCode =  member.getAffiliatedCode();
    }
}