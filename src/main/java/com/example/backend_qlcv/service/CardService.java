package com.example.backend_qlcv.service;


import com.example.backend_qlcv.entity.Card;

import java.util.List;

public interface CardService {
    List<Card> getAll();

    Card add(Card card);

    Card update(Card card, Long id);

    Card detail(Long id);

    void delete(Long id);

    void addMemberToCard(Long cardId, Long userId);
}
