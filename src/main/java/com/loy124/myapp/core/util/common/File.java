package com.loy124.myapp.core.util.common;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class File {

    private String original_file_name;
    private String file_name;

    @Builder
    public File(String original_file_name, String file_name) {
        this.original_file_name = original_file_name;
        this.file_name = file_name;
    }
}
