package com.example.file_upload_demo.controller;

import com.example.file_upload_demo.entity.Upload;
import com.example.file_upload_demo.exception.UploadException;
import com.example.file_upload_demo.service.UploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class FileUploadController {

    @Autowired
    private UploadService uploadService;

    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            Upload upload = uploadService.uploadFile(file);
            model.addAttribute("message", "Upload thành công: " + upload.getFileName());
        } catch (UploadException e) {
            model.addAttribute("message", "Lỗi: " + e.getMessage());
        }
        return "uploadResult";
    }

    @GetMapping("/uploads")
    public String listUploads(Model model) {
        List<Upload> uploads = uploadService.getAllUploads();
        model.addAttribute("uploads", uploads);
        return "list";
    }
}