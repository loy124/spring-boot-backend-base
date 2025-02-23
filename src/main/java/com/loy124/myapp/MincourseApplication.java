package com.loy124.myapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@Slf4j
@EnableJpaAuditing
@EnableFeignClients
public class MincourseApplication {

	@Value("${my.nickname}")
	private String naming;

	@Bean
	public void Test(){
		log.info("naming={}", naming);
	}
	public static void main(String[] args) {
		SpringApplication.run(MincourseApplication.class, args);
	}

}
