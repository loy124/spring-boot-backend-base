package com.loy124.myapp.core.util.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDto<T> {
    private Integer status; // 에러시에 의미 있음.
    private String msg; // 에러시에 의미 있음. ex) badRequest
    private T data; // 에러시에는 구체적인 에러 내용 ex) username이 입력되지 않았습니다

    public ResponseDto(){
        this.status = HttpStatus.OK.value();
        this.msg = "ok";
    }

    public ResponseDto(HttpStatus httpStatus, String msg){
        this.status = httpStatus.value();
        this.msg = msg; // 에러 제목
    }

    public ResponseDto(T data){
        this.status = HttpStatus.OK.value();
        this.msg = "ok";
        this.data = data; // 응답할 데이터 바디
    }


    public ResponseDto(HttpStatus httpStatus, String msg, T data){
        this.status = httpStatus.value();
        this.msg = msg; // 에러 제목
        this.data = data; // 에러 내용
    }
}
