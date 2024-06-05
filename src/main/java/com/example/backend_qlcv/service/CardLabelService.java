package com.example.backend_qlcv.service;

import com.example.backend_qlcv.entity.CardLabel;

import java.util.List;

public interface CardLabelService {
    List<CardLabel> getAll();

    CardLabel add(CardLabel cardLabel);

    CardLabel update(CardLabel cardLabel, Long id);

    CardLabel detail(Long id);

    void delete(Long id);
}
