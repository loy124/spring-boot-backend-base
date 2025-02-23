package com.loy124.myapp.member.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminManagerPageRequestDto {
    String searchText;
    String domainName;
}
