package com.loy124.myapp.member.dto;

import com.loy124.myapp.member.entity.Member;
import lombok.Data;

/**
 * DTO for {@link Member}
 */
@Data
public class AdminGetMemberTotalCountRequestDto {
    String domainName;

    public AdminGetMemberTotalCountRequestDto(String domainName) {
        this.domainName = domainName;
    }

    public AdminGetMemberTotalCountRequestDto() {
    }
}