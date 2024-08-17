package com.example.backend_qlcv.service;

import com.example.backend_qlcv.entity.CardLabel;
import com.example.backend_qlcv.entity.Label;

import java.util.List;

public interface CardLabelService {
    List<CardLabel> getAll();

    List<Label> getLabelsByCard(Long cardId);

    CardLabel add(CardLabel cardLabel);

    CardLabel update(CardLabel cardLabel, Long id);

    CardLabel detail(Long id);

    void delete(Long id);

    void addColorsToCard(Long cardId, List<Long> labelIds);

    void removeLabelByCard(Long cardId, Long labelId);

    void removeLabelByLabelId(Long labelId);
}
