package com.loy124.myapp.core.util.mail;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url = "https://api.mailgun.net/v3/")
@Qualifier(value = "mailgun")
public interface MailgunClient {

//    @PostMapping("sandbox9f3f3f0bdaae458c85456b7307aeb932.mailgun.org/messages")
//    ResponseEntity<String> sendEmail(@SpringQueryMap MailForm from);


    @PostMapping("aa/messages")
    ResponseEntity<String> sendEmail(@SpringQueryMap MailForm from);

}
