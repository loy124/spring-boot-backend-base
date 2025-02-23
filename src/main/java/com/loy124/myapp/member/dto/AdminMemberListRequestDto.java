package com.loy124.myapp.member.dto;

import com.loy124.myapp.member.entity.Member;
import lombok.Builder;
import lombok.Data;
import com.loy124.myapp.core.util.common.Role;

import java.time.LocalDateTime;

/**
 * DTO for {@link Member}
 */
@Data
@Builder
public class AdminMemberListRequestDto {
    String SearchText;
    String email;
    String name;
    Integer employeeNumber;
    String affiliatedCode;
    Role role;
    String phoneNumber;
    LocalDateTime deletedDate;
    String domainName;
}