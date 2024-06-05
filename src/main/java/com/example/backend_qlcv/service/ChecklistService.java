package com.example.backend_qlcv.service;


import com.example.backend_qlcv.entity.Checklist;

import java.util.List;

public interface ChecklistService {
    List<Checklist> getAll();

    Checklist add(Checklist checklist);

    Checklist update(Checklist checklist, Long id);

    Checklist detail(Long id);

    void delete(Long id);
}
