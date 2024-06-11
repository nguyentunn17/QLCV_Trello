package com.example.backend_qlcv.service;

import com.example.backend_qlcv.entity.History;
import com.example.backend_qlcv.entity.User;

import java.util.List;

public interface HistoryService {
    List<History> getAll();

    History add(History history);

    History update(History history, Long id);

    History detail(Long id);

    void delete(Long id);

    void record(String tableName, Long recordId, String action, String changeDescription, Long userId);

}
