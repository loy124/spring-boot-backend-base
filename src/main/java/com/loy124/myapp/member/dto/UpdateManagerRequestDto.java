package com.loy124.myapp.member.dto;

import com.loy124.myapp.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import com.loy124.myapp.core.util.common.Role;

/**
 * DTO for {@link Member}
 */
@Value
@Builder
public class UpdateManagerRequestDto {
    @NotBlank
    String email;
    @NotBlank
    String name;

    String password;

    String phoneNumber;

    @NotNull
    Role role;
}