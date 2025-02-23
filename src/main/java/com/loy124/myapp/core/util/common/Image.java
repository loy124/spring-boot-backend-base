package com.loy124.myapp.core.util.common;


import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Image {
    private String original_image_name;
    private String image_name;

    @Builder
    public Image(String original_image_name, String image_name) {
        this.original_image_name = original_image_name;
        this.image_name = image_name;

    }
}
