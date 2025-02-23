package com.loy124.myapp.member.dto;

import com.loy124.myapp.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for {@link Member}
 */
@Value
@Builder
public class CreateManagerRequestDto {
    @NotBlank
    String email;
    @NotBlank
    String name;
    @NotBlank
    String password;
    String phoneNumber;
    String domainName;

}