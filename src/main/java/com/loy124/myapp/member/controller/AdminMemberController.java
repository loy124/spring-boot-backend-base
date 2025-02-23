package com.loy124.myapp.member.controller;

import com.loy124.myapp.core.util.dto.ResponseDto;
import com.loy124.myapp.member.dto.*;
import com.loy124.myapp.member.entity.Member;
import com.loy124.myapp.member.service.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/member")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;


    @GetMapping("/v1")
    public ResponseEntity<?> getMemberList(AdminMemberListRequestDto adminMemberListRequestDto, Pageable pageable) {

        Page<Member> memberPage = adminMemberService.getMemberList(adminMemberListRequestDto, pageable);

        List<AdminMemberListResponseDto> adminMemberListResponseDtoList = memberPage.getContent().stream().map(AdminMemberListResponseDto::new).toList();

        Page<AdminMemberListResponseDto> adminMemberListResponseDtoPage = new PageImpl<>(adminMemberListResponseDtoList, memberPage.getPageable(), memberPage.getTotalElements());

        ResponseDto<Page<AdminMemberListResponseDto>> responseDto = new ResponseDto<>(adminMemberListResponseDtoPage);

        return ResponseEntity.ok().body(responseDto);

    }




    //로그인에 들어있는 권한따라 다르게 진행하기
    @GetMapping("/manager/v1")
    public ResponseEntity<?> getManagerPage(AdminManagerPageRequestDto adminManagerPageRequestDto, Pageable pageable){

        Page<Member> managerPage = adminMemberService.getManagerPage(adminManagerPageRequestDto, pageable);


        List<AdminManagerPageResponseDto> adminGradeManagementResponseDtoList = managerPage.getContent().stream().map(AdminManagerPageResponseDto::new).toList();

        Page<AdminManagerPageResponseDto> adminManagerPageResponseDtoPage = new PageImpl<>(adminGradeManagementResponseDtoList, managerPage.getPageable(), managerPage.getTotalElements());

        return ResponseEntity.ok().body(new ResponseDto<>(adminManagerPageResponseDtoPage));

    }



    @PostMapping("/manager/v1")
    public ResponseEntity<?> createManager(@RequestBody CreateManagerRequestDto createManagerRequestDto){

        adminMemberService.createManager(createManagerRequestDto);

        ResponseDto<String> responseDto = new ResponseDto<>("회원가입이 완료되었습니다.");

        return ResponseEntity.ok().body(responseDto);

    }

    @PostMapping("/v1")
    public ResponseEntity<?> createMember(@RequestBody CreateMemberRequestDto createMemberRequestDto){

        adminMemberService.createMember(createMemberRequestDto);

        ResponseDto<String> responseDto = new ResponseDto<>("회원가입이 완료되었습니다.");

        return ResponseEntity.ok().body(responseDto);

    }

    @GetMapping("/manager/{managerId}/v1")
    public ResponseEntity<?> getManager(@PathVariable("managerId") Long managerId){

        Member member = adminMemberService.getManager(managerId);
        AdminGetManagerResponseDto adminGetManagerResponseDto = new AdminGetManagerResponseDto(member);
        ResponseDto<AdminGetManagerResponseDto> responseDto = new ResponseDto<>(adminGetManagerResponseDto);

        return ResponseEntity.ok().body(responseDto);

    }

    @PatchMapping("/manager/{managerId}/v1")
    public ResponseEntity<?> updateManager(@RequestBody UpdateManagerRequestDto updateManagerRequestDto, @PathVariable("managerId") Long managerId){

        adminMemberService.updateManager(updateManagerRequestDto, managerId);

        ResponseDto<String> responseDto = new ResponseDto<>("매니저 정보 수정이 완료되었습니다.");

        return ResponseEntity.ok().body(responseDto);

    }

    @DeleteMapping("/manager/{managerId}/v1")
    public ResponseEntity<?> deleteManager(@PathVariable("managerId") Long managerId){

        adminMemberService.deleteManager(managerId);

        ResponseDto<String> responseDto = new ResponseDto<>("매니저 삭제가 완료되었습니다.");

        return ResponseEntity.ok().body(responseDto);

    }



    @PatchMapping("/{memberId}/role/v1")
    public ResponseEntity<?> updateGradeManagement(@PathVariable("memberId") Long memberId){

        adminMemberService.updateGrade(memberId);

        ResponseDto<String> responseDto = new ResponseDto<>("회원 등업이 완료되었습니다.");

        return ResponseEntity.ok().body(responseDto);

    }




    @GetMapping("/{memberId}/v1")
    public ResponseEntity<?> getMember(@PathVariable("memberId") Long memberId){

        Member member = adminMemberService.getMember(memberId);

        AdminGetMemberResponseDto adminGetManagerResponseDto = new AdminGetMemberResponseDto(member);

        ResponseDto<AdminGetMemberResponseDto> responseDto = new ResponseDto<>(adminGetManagerResponseDto);

        return ResponseEntity.ok().body(responseDto);

    }

    @PatchMapping("/{memberId}/v1")
    public ResponseEntity<?> updateMember(@RequestBody AdminUpdateRequestDto adminUpdateRequestDto, @PathVariable("memberId") Long memberId){

        adminMemberService.updateMember(adminUpdateRequestDto, memberId);

        ResponseDto<String> responseDto = new ResponseDto<>("회원정보 업데이트가 완료되었습니다");

        return ResponseEntity.ok().body(responseDto);

    }

    @DeleteMapping("/{memberId}/v1")
    public ResponseEntity<?> deleteMember(@PathVariable("memberId") Long memberId){

        adminMemberService.deleteMember(memberId);

        ResponseDto<String> responseDto = new ResponseDto<>("회원 삭제가 완료되었습니다.");

        return ResponseEntity.ok().body(responseDto);

    }


    @GetMapping("/dashboard/count/v1")
    public ResponseEntity<?> getManagerCount(AdminGetMemberTotalCountRequestDto adminGetMemberTotalCountRequestDto){
        Long memberCount = adminMemberService.getAdminMemberCount(adminGetMemberTotalCountRequestDto);

        AdminGetMemberTotalCountResponseDto adminGetMemberTotalCountResponseDto = new AdminGetMemberTotalCountResponseDto(memberCount);

        ResponseDto<?> responseDto = new ResponseDto<>(adminGetMemberTotalCountResponseDto);

        return ResponseEntity.ok().body(responseDto);

    }




}







