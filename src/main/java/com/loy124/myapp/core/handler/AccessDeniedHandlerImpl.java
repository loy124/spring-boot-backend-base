package com.loy124.myapp.core.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import com.loy124.myapp.core.util.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.loy124.myapp.core.util.exception.ErrorException.*;

@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("권한이 없는 사용자입니다. : {}", accessDeniedException.getMessage());
        response.setStatus(403);
        response.setContentType("application/json; charset=utf-8");
        Exception403 exception403 = new Exception403(accessDeniedException.getMessage());
        ResponseDto<?> responseDto = new ResponseDto<>(HttpStatus.FORBIDDEN, "forbidden", exception403.getMessage());
        ObjectMapper om = new ObjectMapper();
        String responseBody = om.writeValueAsString(responseDto);
        response.getWriter().println(responseBody);
    }
}
