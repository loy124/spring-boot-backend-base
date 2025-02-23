package com.loy124.myapp.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loy124.myapp.core.util.auth.jwt.JwtTokenUtil;
import com.loy124.myapp.member.entity.Member;
import com.loy124.myapp.member.repository.MemberRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MemberUtil {
    public static <T> String toJson(ObjectMapper objectMapper, T data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }

    private static String token(BCryptPasswordEncoder bCryptPasswordEncoder, MemberRepository memberRepository, String key, Member member) {
        member.encodedPassword(bCryptPasswordEncoder);
        Member saveAdmin = memberRepository.save(member);

        return JwtTokenUtil.createRefreshToken(saveAdmin, key);
    }

    public static String token(String key, Member member) {
//        member.encodedPassword(bCryptPasswordEncoder);

        return JwtTokenUtil.createRefreshToken(member, key);
    }

}
