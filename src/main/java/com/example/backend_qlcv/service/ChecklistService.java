package com.example.backend_qlcv.service;


import com.example.backend_qlcv.entity.Checklist;
import com.example.backend_qlcv.entity.Lists;

import java.util.List;

public interface ChecklistService {
    List<Checklist> getAll();

    Checklist add(Checklist checklist);

    Checklist update(Checklist checklist, Long id);

    Checklist detail(Long id);

    void delete(Long checklistId);

    List<Checklist> getChecklistByCardId(Long cardId);
}
