package com.loy124.myapp.member.dto;

import com.loy124.myapp.member.entity.Member;
import lombok.Builder;
import lombok.Value;
import com.loy124.myapp.core.util.common.Role;

/**
 * DTO for {@link Member}
 */
@Value
@Builder
public class AdminUpdateRequestDto {
    String name;
    Integer employeeNumber;
    String affiliatedCode;
    String phoneNumber;
    String password;
    Role role;

}