package com.loy124.myapp.core.filter;


import com.loy124.myapp.core.util.auth.MemberDetails;
import com.loy124.myapp.core.util.auth.jwt.JwtStatus;
import com.loy124.myapp.core.util.auth.jwt.JwtTokenUtil;
import com.loy124.myapp.core.util.exception.ErrorException;
import com.loy124.myapp.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


import com.loy124.myapp.core.util.common.Role;
import com.loy124.myapp.member.entity.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;


@Slf4j
public class JwtAuthenticateFilter extends BasicAuthenticationFilter {


    private MemberRepository memberRepository;

    private String key;


    public JwtAuthenticateFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public JwtAuthenticateFilter(AuthenticationManager authenticationManager, String key, MemberRepository memberRepository) {
        super(authenticationManager);
        this.key = key;
        this.memberRepository = memberRepository;
    }

    public JwtAuthenticateFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        //accessToken
        String refreshToken = getRefreshTokenFromCookie(request);

        log.info("auth={}, refresh ={}", authorization, refreshToken);

        if(!StringUtils.hasText(authorization)&& !StringUtils.hasText(refreshToken)){
            JwtTokenUtil.logout(response, key);
            chain.doFilter(request, response);
            return;
        }


        try{
            String accessToken = null;
            JwtStatus isVerifyAccessToken = null;

            if(StringUtils.hasText(authorization)) {
                //Token 꺼내기
                accessToken = authorization.replace(JwtTokenUtil.TOKEN_PREFIX, "");
                isVerifyAccessToken = JwtTokenUtil.verify(accessToken, key);
                log.info("accessResult={}",isVerifyAccessToken.toString());
            }

            JwtStatus isVerifyRefreshToken = null;
            if(StringUtils.hasText(refreshToken)) {
                isVerifyRefreshToken = JwtTokenUtil.verify(refreshToken, key);
                log.info("refreshResult={}", isVerifyRefreshToken);
            }

            //access와 refresh가 유효하지 않는 경우
            if(isVerifyAccessToken != JwtStatus.TRUE && isVerifyRefreshToken != JwtStatus.TRUE){
                chain.doFilter(request, response);
                return;
            }

                //access 토큰이 유효한경우
                if(isVerifyAccessToken == JwtStatus.TRUE){
                    //refresh 토큰이 없거나 만료되었을 경우 refresh 토큰 재발급

                    if(!StringUtils.hasText(refreshToken) || isVerifyRefreshToken == JwtStatus.EXPIRED){
                        log.info("refresh 토큰 재발급");
                        logger.error(accessToken);
                        Member member = getMember(accessToken);

                        refreshToken = JwtTokenUtil.setRefreshToken(member, response, key);

                        isVerifyRefreshToken = JwtStatus.TRUE;
                    }

                }

                //refresh 토큰이 유효할때
                if(isVerifyRefreshToken == JwtStatus.TRUE){

                    if(!StringUtils.hasText(accessToken)|| isVerifyAccessToken == JwtStatus.EXPIRED){
                        log.info("accessToken 재발급");
                        Member member = getMember(refreshToken);

                        accessToken = JwtTokenUtil.setAccessToken(member, response, key);
                        isVerifyAccessToken = JwtStatus.TRUE;
                    }
                }


                if(isVerifyRefreshToken == JwtStatus.TRUE && isVerifyAccessToken == JwtStatus.TRUE){

                    Member member = getMember(refreshToken);
                    Role role = member.getRole();

                    MemberDetails myMemberDetails = new MemberDetails(member);

//                    String roleName = "ROLE_" + role.name();
//                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
//                    인증이 끝나서 SecuriyCOntextHolder.getContext 에 등록될 Auth 객체

//                    Collection<GrantedAuthority> authorities = new ArrayList<>();
//                    authorities.add(authority);

                    //UsernamePasswordAuthenticationToken 은 Authentication을 상속한다.
                    Authentication authentication = new UsernamePasswordAuthenticationToken(myMemberDetails, null, myMemberDetails.getAuthorities());


                    SecurityContextHolder.getContext().setAuthentication(authentication);


                }


        }catch(Exception e){

            logger.error(e);
        } finally {
            chain.doFilter(request, response);
        }
    }

    private Member getMember(String token) {
        Claims body = JwtTokenUtil.getBody(token, key);
        Long id  = body.get("id", Long.class);
        String email  = body.get("email", String.class);
        String roleString = body.get("role", String.class);
        Role role = Role.valueOf(roleString);
//        DB로부터 member 정보 가져오기

//        Member member = Member.builder().id(id).email(email).role(role).build();
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        return memberOptional.orElseThrow(()-> new ErrorException.Exception404("해당 이메일을 찾을수 없습니다"));
    }


    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                //refresh token 이름
                if (cookie.getName().equals("min-token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }



//    private String getEmail(String token) {
//        return JwtTokenUtil.getEmail(token, key);
//    }

}


