package com.loy124.myapp.member.controller;

import com.loy124.myapp.core.util.auth.jwt.JwtTokenUtil;
import com.loy124.myapp.member.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.loy124.myapp.core.util.dto.ResponseDto;
import com.loy124.myapp.member.entity.Member;
import com.loy124.myapp.member.service.MemberService;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Value("${jwt.token.secret}")
    private String key;

    //email-check
    @GetMapping("/email-check/v1")
    public ResponseEntity<?> emailCheck(@Valid @RequestParam String email) {

        memberService.emailCheck(email);

        ResponseDto<String> responseDto = new ResponseDto<>("회원가입이 가능한 이메일입니다.");

        return ResponseEntity.ok().body(responseDto);


    }

    //회원가입

    @PostMapping("/v1")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        memberService.signup(signUpRequestDto);

        ResponseDto<?> responseDTO = new ResponseDto<>("회원가입에 성공하였습니다.");

        return ResponseEntity.ok().body(responseDTO);

    }
    @PatchMapping ("/v1")
    public ResponseEntity<?> update(@RequestBody @Valid UpdateRequestDto updateRequestDto) {

        memberService.update(updateRequestDto);
        ResponseDto<?> responseDTO = new ResponseDto<>("정보 수정에 성공하였습니다.");
        return ResponseEntity.ok().body(responseDTO);
    }

    //login

    @PostMapping("/login/v1")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response) {

        Member member = memberService.login(loginRequestDto, request, response);

        JwtTokenUtil.setAccessToken(member, response, key);

        JwtTokenUtil.setRefreshToken(member, response, key);

        LoginResponseDto loginResponseDto = new LoginResponseDto(member);

        return ResponseEntity.ok().body(new ResponseDto<>(loginResponseDto));
    }

    @PostMapping("/logout/v1")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        JwtTokenUtil.logout(response, "min-token");

        ResponseDto<?> responseDTO = new ResponseDto<>("로그아웃에 성공하였습니다.");
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/v1")
    public ResponseEntity<?> withdrawal(HttpServletRequest request, HttpServletResponse response) {
        memberService.withdrawal();
        JwtTokenUtil.logout(response, "min-token");

        ResponseDto<?> responseDTO = new ResponseDto<>("회원 탈퇴에 성공하였습니다.");
        return ResponseEntity.ok().body(responseDTO);
    }


    @PostMapping("/reset-password/v1")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordMemberDto resetPasswordMemberDto) {
        //메일로 변경된 패스워드 전송하기
        memberService.resetPassword(resetPasswordMemberDto);

        ResponseDto<?> responseDTO = new ResponseDto<>("이메일 전송에 성공하였습니다.");
        return ResponseEntity.ok().body(responseDTO);
    }


//    login된 상태를 유지해야한다


    @PostMapping("/silent-refresh/v1")
    public ResponseEntity<?> silentRefresh(HttpServletRequest request, HttpServletResponse response) {

        Member member = memberService.getSilentRefreshMember();

        if(member == null){
            return ResponseEntity.ok().body(new ResponseDto<>());
        }

        JwtTokenUtil.setAccessToken(member, response, key);

        LoginResponseDto loginResponseDto = new LoginResponseDto(member);



        return ResponseEntity.ok().body(new ResponseDto<>(loginResponseDto));

    }



}











