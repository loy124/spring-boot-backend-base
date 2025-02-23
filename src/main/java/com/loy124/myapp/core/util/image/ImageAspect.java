package com.loy124.myapp.core.util.image;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;

@Aspect
@Component
@Slf4j
public class ImageAspect {


    @Around("@annotation(com.loy124.myapp.core.util.image.ImageConvertSrc)")
    public Object doAnnotation(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("되나");
        Object result = joinPoint.proceed();

        log.info("되나");
        ResponseEntity response = (ResponseEntity) result;
        Object body = response.getBody();

        //어떤 DTO가 들어올지 모르지만 그안에 src는 있을테니 그걸 리플렉션으로 해결해보기
        Class<?> dtoClass = body.getClass();


        if(body instanceof ArrayList){
            log.info("리스트");
            ArrayList list = (ArrayList) body;


            for (Object o : list) {
                Class<?> aClass = o.getClass();
                Field[] fields = aClass.getDeclaredFields();
                findFieldsAndPushSrc(o, fields);

            }



        }else{
            log.info("리스트아님 ");

            Field[] fields = dtoClass.getDeclaredFields();


            findFieldsAndPushSrc(body, fields);

        }


        return result;
    }

    private static void findFieldsAndPushSrc(Object body, Field[] fields) {
        for (Field field : fields) {
            // 필드의 이름을 얻습니다.
            String fieldName = field.getName();
            field.setAccessible(true);
            try {
                // 필드의 값을 가져옵니다.
                Object fieldValue = field.get(body);

//                // 여기에서 필요한 작업을 수행합니다.
//                System.out.println("Field Name: " + fieldName);
//                System.out.println("Field Value: " + fieldValue);
//
//                if(fieldName.equals("img") && field.getType() == Image.class){
//                    System.out.println("ㄹㅇ ㅋㅋ");
//                    field.set("newSRC", "OO");
//                }


            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
    }

}
