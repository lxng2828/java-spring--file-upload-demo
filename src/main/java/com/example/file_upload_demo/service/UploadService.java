package com.example.file_upload_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.file_upload_demo.entity.Upload;
import com.example.file_upload_demo.exception.UploadException;
import com.example.file_upload_demo.repository.UploadRepository;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class UploadService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    private UploadRepository uploadRepository;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".png");

    public Upload uploadFile(MultipartFile file) throws UploadException {
        if (file.isEmpty()) {
            throw new UploadException("Vui lòng chọn một file!");
        }
        String fileName = file.getOriginalFilename();
        if (!isAllowedExtension(fileName)) {
            throw new UploadException("Chỉ hỗ trợ file .jpg hoặc .png!");
        }
        try {
            ensureUploadDirectoryExists();
            String uniqueFileName = generateUniqueFileName(fileName);
            File dest = new File(uploadDir + File.separator + uniqueFileName);
            file.transferTo(dest);
            return saveUpload(uniqueFileName);
        } catch (IOException e) {
            throw new UploadException("Lỗi khi upload file: " + e.getMessage());
        }
    }

    public List<Upload> getAllUploads() {
        List<Upload> uploads = uploadRepository.findAll();
        for (Upload upload : uploads) {
            File file = new File(uploadDir + File.separator + upload.getFileName());
            upload.setFileName(file.exists() ? upload.getFileName() : null);
        }
        return uploads;
    }

    private boolean isAllowedExtension(String fileName) {
        if (fileName == null)
            return false;
        String lowerName = fileName.toLowerCase();
        return ALLOWED_EXTENSIONS.stream().anyMatch(lowerName::endsWith);
    }

    private void ensureUploadDirectoryExists() {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
            dir.setWritable(true, false);
        }
    }

    private String generateUniqueFileName(String originalName) {
        return UUID.randomUUID() + "_" + originalName;
    }

    private Upload saveUpload(String fileName) {
        Upload upload = new Upload();
        upload.setFileName(fileName);
        upload.setUploadTime(LocalDateTime.now());
        return uploadRepository.save(upload);
    }
}
