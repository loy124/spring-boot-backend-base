package com.loy124.myapp.core.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import com.loy124.myapp.core.util.dto.ResponseDto;
import com.loy124.myapp.core.util.exception.ErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("인증되지 않은 사용자의 접근입니다 : {}", authException.getMessage());
        response.setStatus(401);
        response.setContentType("application/json; charset=utf-8");
        ErrorException.Exception401 exception401 = new ErrorException.Exception401(authException.getMessage());
        ResponseDto<?> responseDto = new ResponseDto<>(HttpStatus.UNAUTHORIZED, "unAuthorized", exception401.getMessage());
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDto);
        response.getWriter().println(responseBody);

    }
}
