package com.loy124.myapp.etc.controller;

import com.loy124.myapp.core.util.common.Image;
import com.loy124.myapp.core.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.loy124.myapp.etc.service.EtcService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/etc")
public class EtcController {

    private final EtcService etcService;

    @PostMapping("/ck_upload_image/v1")
    public ResponseEntity<?> uploadImage(@RequestPart(required = false) MultipartFile upload) throws IOException {

        Image uploadedImage = etcService.create(upload);

        ResponseDto<Image> imageResponseDto = new ResponseDto<>(uploadedImage);

        return ResponseEntity.ok().body(imageResponseDto);

    }



}
