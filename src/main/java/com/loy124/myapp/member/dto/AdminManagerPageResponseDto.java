package com.loy124.myapp.member.dto;

import lombok.Value;
import com.loy124.myapp.core.util.common.Role;
import com.loy124.myapp.member.entity.Member;

import java.time.LocalDateTime;

/**
 * DTO for {@link Member}
 */
@Value
public class AdminManagerPageResponseDto {

    Long id;
    String email;
    String name;
    Role role;
    LocalDateTime createdDate;
    LocalDateTime lastLogin;

    public AdminManagerPageResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.role = member.getRole();
        this.lastLogin = member.getLastLogin();
        this.createdDate = member.getCreatedDate();
    }
}