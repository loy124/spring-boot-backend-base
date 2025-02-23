package com.loy124.myapp.member.service;

import com.loy124.myapp.core.util.auth.MemberDetails;
import com.loy124.myapp.core.util.common.Role;
import com.loy124.myapp.core.util.exception.ErrorException;
import com.loy124.myapp.member.dto.*;
import com.loy124.myapp.member.entity.Member;
import com.loy124.myapp.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    public Boolean emailCheck(String email) {

        Boolean existsByEmail = memberRepository.existsByEmail(email);

        if(existsByEmail == Boolean.TRUE){
            throw new ErrorException.Exception409("중복된 이메일입니다.");
        }

        return existsByEmail;
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @Transactional
    public Page<Member> getMemberList(AdminMemberListRequestDto adminMemberListRequestDto, Pageable pageable) {

        return memberRepository.adminMemberListPage(adminMemberListRequestDto, pageable);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @Transactional
    public Member getManager(Long memberId) {
        return memberRepository.findById(memberId)
                 .orElseThrow(() -> new ErrorException.Exception404("해당 멤버가 존재 하지 않습니다."));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @Transactional
    public Member getMember(Long memberId) {
        return memberRepository.adminFindByIdFetchJoin(memberId)
                .orElseThrow(() -> new ErrorException.Exception404("해당 멤버가 존재 하지 않습니다."));
    }



    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @Transactional
    public void updateMember(AdminUpdateRequestDto adminUpdateRequestDto, Long memberId) {
//            호출 -> 저장
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorException.Exception404("해당 멤버가 존재 하지 않습니다."));


        member.updateMember(adminUpdateRequestDto);

        if(adminUpdateRequestDto.getPassword() != null){
            member.updatePassword(adminUpdateRequestDto.getPassword());
            member.encodedPassword(bCryptPasswordEncoder);
        }

        //TODO  강의 권한 설정


    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorException.Exception404("해당 멤버가 존재 하지 않습니다."));
        memberRepository.delete(member);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @Transactional
    public void updateGrade(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorException.Exception404("해당 멤버가 존재 하지 않습니다."));

        if(member.getRole() != Role.PENDING_EMPLOYEE){
            throw new ErrorException.Exception403("등업시킬수 없는 회원입니다.");
        }

        member.updateRoleEmployee();


    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @Transactional
    public Page<Member> getManagerPage(AdminManagerPageRequestDto adminManagerPageRequestDto, Pageable pageable) {
//        //로그인 정보로부터 관리자인 경우 domain null 주입
//        Member member = getMember();
//
//        // ADMIN인 경우는 모든 검색 그게 아닌 경우 각 도메인별 검색
//        if(member.getRole() != Role.ADMIN){
//            Domain domain = member.getDomain();
//            adminManagerPageRequestDto.setDomainName(domain.getName());
//        }


        return memberRepository.adminManagerListPage(adminManagerPageRequestDto, pageable);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @Transactional
    public void createManager(CreateManagerRequestDto createManagerRequestDto) {

        emailCheck(createManagerRequestDto.getEmail());
        Member member = Member.builder()
                .email(createManagerRequestDto.getEmail())
                .name(createManagerRequestDto.getName())
                .password(createManagerRequestDto.getPassword())
                .phoneNumber(createManagerRequestDto.getPhoneNumber())
                .role(Role.MANAGER)
                .build();

        String domainName = createManagerRequestDto.getDomainName();


        member.encodedPassword(bCryptPasswordEncoder);
        memberRepository.save(member);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void createMember(CreateMemberRequestDto createManagerRequestDto) {

        emailCheck(createManagerRequestDto.getEmail());
        Member member = Member.builder()
                .email(createManagerRequestDto.getEmail())
                .name(createManagerRequestDto.getName())
                .password(createManagerRequestDto.getPassword())
                .phoneNumber(createManagerRequestDto.getPhoneNumber())
                .role(createManagerRequestDto.getRole())
                .build();

        String domainName = createManagerRequestDto.getDomainName();


        member.encodedPassword(bCryptPasswordEncoder);
        memberRepository.save(member);

    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @Transactional
    public void updateManager(UpdateManagerRequestDto updateManagerRequestDto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorException.Exception404("해당 매니저가 존재 하지 않습니다."));
        if(!member.getEmail().equals(updateManagerRequestDto.getEmail())){
            emailCheck(updateManagerRequestDto.getEmail());
        }


        member.updateMember(updateManagerRequestDto);
        if(updateManagerRequestDto.getPassword() != null){
            member.updatePassword(updateManagerRequestDto.getPassword());
            member.encodedPassword(bCryptPasswordEncoder);
        }



    }


    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @Transactional
    public void deleteManager(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorException.Exception404("해당 멤버가 존재 하지 않습니다."));
        memberRepository.delete(member);
    }


    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @Transactional
    public Long getAdminMemberCount(AdminGetMemberTotalCountRequestDto adminGetMemberTotalCountRequestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() == "anonymousUser") {
            throw new ErrorException.Exception404("로그인 되어있는 유저가 없습니다.");
        }

        return memberRepository.getAdminMemberCount(adminGetMemberTotalCountRequestDto);
    }



    public Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() == "anonymousUser") {
            throw new ErrorException.Exception404("로그인 되어있는 유저가 없습니다.");
        }

        MemberDetails principal = (MemberDetails) authentication.getPrincipal();

        return memberRepository.findByEmail(principal.getEmail())
                .orElseThrow(() -> new ErrorException.Exception404("해당 유저가 없습니다."));
    }

}










