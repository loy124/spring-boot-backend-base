package com.loy124.myapp.member.repository;


import com.loy124.myapp.member.dto.AdminMemberListRequestDto;
import com.loy124.myapp.member.dto.AdminGetMemberTotalCountRequestDto;
import com.loy124.myapp.member.dto.AdminManagerPageRequestDto;
import com.loy124.myapp.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<List<Member>> adminMemberList(AdminMemberListRequestDto adminMemberListRequestDto);

    Page<Member> adminMemberListPage(AdminMemberListRequestDto adminMemberListRequestDto, Pageable pageable);


    Optional<Member> adminFindByIdFetchJoin(Long memberId);


    Page<Member> adminManagerListPage(AdminManagerPageRequestDto adminManagerPageRequestDto, Pageable pageable);


    Long getAdminMemberCount(AdminGetMemberTotalCountRequestDto adminGetMemberTotalCountRequestDto);


}
