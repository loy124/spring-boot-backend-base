package com.loy124.myapp.member.dto;

import com.loy124.myapp.member.entity.Member;
import lombok.Data;

/**
 * DTO for {@link Member}
 */
@Data
public class AdminGetMemberTotalCountResponseDto {
    Long totalMemberCount;

    public AdminGetMemberTotalCountResponseDto(Long totalMemberCount) {
        this.totalMemberCount = totalMemberCount;
    }
}