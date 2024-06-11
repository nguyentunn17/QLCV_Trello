package com.example.backend_qlcv.service;



import com.example.backend_qlcv.entity.Lists;

import java.util.List;

public interface ListService {
    List<Lists> getAll();

    Lists add(Lists lists);

    Lists update(Lists lists, Long id);

    Lists detail(Long id);

    void delete(Long id);

    void archiveList(Long listId, Long userId);

    void restoreList(Long listId, Long userId);
}
