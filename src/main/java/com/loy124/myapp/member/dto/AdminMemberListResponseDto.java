package com.loy124.myapp.member.dto;

import com.loy124.myapp.core.util.common.Role;
import com.loy124.myapp.member.entity.Member;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link Member}
 */
@Value
public class AdminMemberListResponseDto {
    LocalDateTime createdDate;
    LocalDateTime lastModifiedDate;
    Long id;
    String email;
    String name;
    Integer employeeNumber;
    String affiliatedCode;
    Role role;
    String language;
    String goal;
    String phoneNumber;
    LocalDateTime lastLogin;
    Boolean isDeleted;
    LocalDateTime deletedDate;

    public AdminMemberListResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.employeeNumber = member.getEmployeeNumber();
        this.affiliatedCode = member.getAffiliatedCode();
        this.role = member.getRole();
        this.language = member.getLanguage();
        this.goal = member.getGoal();
        this.phoneNumber = member.getPhoneNumber();
        this.lastLogin = member.getLastLogin();
        this.isDeleted = member.getIsDeleted();
        this.deletedDate = member.getDeletedDate();
        this.createdDate = member.getCreatedDate();
        this.lastModifiedDate = member.getLastModifiedDate();
    }



}