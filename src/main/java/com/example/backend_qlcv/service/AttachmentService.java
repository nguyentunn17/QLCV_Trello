package com.example.backend_qlcv.service;

import com.example.backend_qlcv.entity.Attachment;

import org.springframework.data.domain.Page;

import java.util.List;

public interface AttachmentService {
    List<Attachment> getAll();

    Attachment add(Attachment attachment);

    Attachment update(Attachment attachment, Long id);

    Attachment detail(Long id);

    void delete(Long id);
}
