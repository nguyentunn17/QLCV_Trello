package com.example.backend_qlcv.service;



import com.example.backend_qlcv.entity.UserBoard;

import java.util.List;

public interface UserBoardService {
    List<UserBoard> getAll();

    UserBoard add(UserBoard userBoard);

    UserBoard update(UserBoard userBoard, Long id);

    UserBoard detail(Long id);

    void delete(Long id);
}
