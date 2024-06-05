package com.example.backend_qlcv.service;

import com.example.backend_qlcv.entity.CardAssignment;

import java.util.List;

public interface CardAssignmentService {
    List<CardAssignment> getAll();

    CardAssignment add(CardAssignment cardAssignment);

    CardAssignment update(CardAssignment cardAssignment, Long id);

    CardAssignment detail(Long id);

    void delete(Long id);
}
