package com.example.backend_qlcv.service;

import com.example.backend_qlcv.entity.History;

import java.util.List;

public interface HistoryService {
    List<History> getAll();

    History add(History history);

    History update(History history, Long id);

    History detail(Long id);

    void delete(Long id);
}
