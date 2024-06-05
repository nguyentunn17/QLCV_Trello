package com.example.backend_qlcv.service;

import com.example.backend_qlcv.entity.ChecklistItem;

import java.util.List;

public interface ChecklistItemService {
    List<ChecklistItem> getAll();

    ChecklistItem add(ChecklistItem checklistItem);

    ChecklistItem update(ChecklistItem checklistItem, Long id);

    ChecklistItem detail(Long id);

    void delete(Long id);
}
