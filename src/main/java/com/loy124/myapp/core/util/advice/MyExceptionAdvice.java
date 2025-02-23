package com.loy124.myapp.core.util.advice;


import lombok.extern.slf4j.Slf4j;

import com.loy124.myapp.core.util.dto.ResponseDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;




import static com.loy124.myapp.core.util.exception.ErrorException.*;

@Slf4j
@RestControllerAdvice
public class MyExceptionAdvice {

//    @MyErrorLog
    @ExceptionHandler(Exception400.class)
    public ResponseEntity<?> badRequest(Exception400 e){
        log.error(e.getMessage());
        return new ResponseEntity<>(e.body(), e.status());
    }

//    @MyErrorLog
    @ExceptionHandler(Exception401.class)
    public ResponseEntity<?> unAuthorized(Exception401 e)
    {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.body(), e.status());
    }

//    @MyErrorLog
    @ExceptionHandler(Exception403.class)
    public ResponseEntity<?> forbidden(Exception403 e)
    {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.body(), e.status());
    }

//    @MyErrorLog
    @ExceptionHandler(Exception404.class)
    public ResponseEntity<?> notFound(Exception404 e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.body(), e.status());
    }
    @ExceptionHandler(Exception409.class)
    public ResponseEntity<?> conflict(Exception409 e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.body(), e.status());
    }

//    @MyErrorLog
    @ExceptionHandler(Exception500.class)
    public ResponseEntity<?> serverError(Exception500 e)
    {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.body(), e.status());

    }

//    @MyErrorLog


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException e){
        log.error(e.getMessage());
//        ResponseDto<String> responseDTO = new ResponseDto<>(HttpStatus.BAD_REQUEST, "notValid",null);
        ResponseDto<String> responseDTO = new ResponseDto<>(HttpStatus.BAD_REQUEST, "notValid",e.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> AccessDeniedError(Exception e){
        log.error(e.getMessage());
        ResponseDto<String> responseDTO = new ResponseDto<>(HttpStatus.FORBIDDEN, "forbidden", e.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> DataIntegrityViolationException(Exception e){
        log.error(e.getMessage());
//        ResponseDto<String> responseDTO = new ResponseDto<>(HttpStatus.FORBIDDEN, "forbidden", "제약 조건을 위반하였습니다.");
        ResponseDto<String> responseDTO = new ResponseDto<>(HttpStatus.FORBIDDEN, "forbidden", e.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknownServerError(Exception e){

            log.error(e.getMessage());
//            ResponseDto<String> responseDTO = new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, "unknownServerError", "에러가 발생하였습니다.");
        ResponseDto<String> responseDTO = new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR, "unknownServerError", e.getMessage());
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
