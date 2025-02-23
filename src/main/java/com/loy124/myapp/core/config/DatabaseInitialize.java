package com.loy124.myapp.core.config;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInitialize implements CommandLineRunner {




    @Override
    public void run(String... args) throws Exception {
        // 여기에서 데이터베이스 초기화 작업을 수행합니다.
        // 예를 들어, 데이터를 생성하거나 가져와서 yourRepository를 통해 저장할 수 있습니다.


    }
}
