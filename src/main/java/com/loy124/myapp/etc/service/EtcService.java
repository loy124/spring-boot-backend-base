package com.loy124.myapp.etc.service;

import com.loy124.myapp.core.util.common.Image;
import com.loy124.myapp.core.util.upload.FileUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EtcService {

    @Value("${upload.dir}")
    private String uploadDir;

    public Image create(MultipartFile image) throws IOException {

        Image uploadImage = null;

        if(image != null) {
            uploadImage = FileUpload.imageUpload(image, uploadDir);

        }
        return uploadImage;

    }
}
