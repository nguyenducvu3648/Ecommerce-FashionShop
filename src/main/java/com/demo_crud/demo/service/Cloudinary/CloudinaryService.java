package com.demo_crud.demo.service.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {
    Cloudinary cloudinary;


    public String uploadImage(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "overwrite", true,
                            "resource_type", "auto"
                    ));
            String secureUrl = (String) uploadResult.get("secure_url");
            if (secureUrl == null) {
                throw new RuntimeException("Không có URL trả về từ việc tải lên Cloudinary");
            }

            return secureUrl;
        } catch (IOException e) {
            throw new RuntimeException("Không thể tải ảnh lên Cloudinary", e);
        }
    }
}
