package com.loy124.myapp.core.util.exception;

import lombok.Getter;

import com.loy124.myapp.core.util.dto.ResponseDto;
import com.loy124.myapp.core.util.dto.ValidDto;
import org.springframework.http.HttpStatus;

public class ErrorException {

    @Getter
    public static class Exception400 extends RuntimeException {

        private String key;
        private String value;

        public Exception400(String key, String value) {
            super(value);
            this.key = key;
            this.value = value;
        }

        public ResponseDto<?> body(){
            ValidDto validDTO = new ValidDto(key, value);
            return new ResponseDto<>(HttpStatus.BAD_REQUEST, "badRequest", validDTO);
        }

        public HttpStatus status(){
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Getter
    public static class Exception401 extends RuntimeException {
        public Exception401(String message) {
            super(message);
        }

        public ResponseDto<?> body(){
            return new ResponseDto<>(HttpStatus.UNAUTHORIZED, "unAuthorized", getMessage());
        }

        public HttpStatus status(){
            return HttpStatus.UNAUTHORIZED;
        }
    }

    @Getter
    public static class Exception403 extends RuntimeException {
        public Exception403(String message) {
            super(message);
        }

        public ResponseDto<?> body(){
            return new ResponseDto<>(HttpStatus.FORBIDDEN, "forbidden", getMessage());
        }

        public HttpStatus status(){
            return HttpStatus.FORBIDDEN;
        }
    }


    @Getter
    public static class Exception404 extends RuntimeException {
        public Exception404(String message) {
            super(message);
        }

        public ResponseDto<?> body(){
            return new ResponseDto<>(HttpStatus.NOT_FOUND, "notFound", getMessage());
        }

        public HttpStatus status(){
            return HttpStatus.NOT_FOUND;
        }
    }



    @Getter
    public static class Exception409 extends RuntimeException {
        public Exception409(String message) {
            super(message);
        }

        public ResponseDto<?> body(){
            return new ResponseDto<>(HttpStatus.CONFLICT, "conflict", getMessage());
        }

        public HttpStatus status(){
            return HttpStatus.CONFLICT;
        }
    }


    @Getter
    public static class Exception500 extends RuntimeException {
        public Exception500(String message) {
            super(message);
        }

        public ResponseDto<?> body(){
            return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, "serverError", getMessage());
        }

        public HttpStatus status(){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
