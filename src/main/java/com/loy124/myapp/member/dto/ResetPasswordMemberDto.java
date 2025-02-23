package com.loy124.myapp.member.dto;

import com.loy124.myapp.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link Member}
 */
@Data
@NoArgsConstructor
public class ResetPasswordMemberDto {
    @NotBlank
    String email;

    public ResetPasswordMemberDto(String email) {
        this.email = email;
    }
}