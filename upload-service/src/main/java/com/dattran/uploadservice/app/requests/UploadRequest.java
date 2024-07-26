package com.dattran.uploadservice.app.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadRequest {
    List<MultipartFile> files;

    @NotNull(message = "Folder must not be null.")
    String folder;

    String groupName;
}
