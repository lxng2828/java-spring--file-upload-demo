package com.example.file_upload_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.file_upload_demo.entity.Upload;

public interface UploadRepository extends JpaRepository<Upload, Long> {
}