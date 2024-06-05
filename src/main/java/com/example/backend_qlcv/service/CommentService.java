package com.example.backend_qlcv.service;

import com.example.backend_qlcv.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getAll();

    Comment add(Comment comment);

    Comment update(Comment comment, Long id);

    Comment detail(Long id);

    void delete(Long id);
}
