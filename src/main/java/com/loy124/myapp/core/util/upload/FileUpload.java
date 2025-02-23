package com.loy124.myapp.core.util.upload;



import com.loy124.myapp.core.util.common.File;
import com.loy124.myapp.core.util.common.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUpload {

    public static Image imageUpload(MultipartFile image, String uploadDir) throws IOException {

        if(image == null){
            return null;
        }

        String originalFilename = image.getOriginalFilename();
        String image_name = UUID.randomUUID() + "_" + originalFilename;

        Path filePath = Paths.get(uploadDir).resolve(image_name);
        Files.copy(image.getInputStream(), filePath);
//변경된이름 <-저장된 이름, 원본이름
        return Image.builder()
                .original_image_name(originalFilename)
                .image_name(image_name)
//                .image_path(filePath.toString())
                .build();

    }

    public static File fileUpload(MultipartFile file, String uploadDir) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String file_name = UUID.randomUUID() + "_" + originalFilename;

        Path filePath = Paths.get(uploadDir).resolve(file_name);
        Files.copy(file.getInputStream(), filePath);


        return File.builder()
                .original_file_name(originalFilename)
                .file_name(file_name)
//                .image_path(filePath.toString())
                .build();

    }

}
