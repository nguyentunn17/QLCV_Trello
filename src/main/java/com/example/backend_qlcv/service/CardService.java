package com.example.backend_qlcv.service;


import com.example.backend_qlcv.entity.Card;
import com.example.backend_qlcv.event.CommentEvent;

import java.util.List;

public interface CardService {
    List<Card> getAll();

    Card add(Card card);

    Card update(Card card, Long id);

    Card detail(Long id);

    void delete(Long id);

    void addMemberToCard(Long cardId, Long userId);

    void handleCommentEvent(CommentEvent event);

    void archiveCard(Long cardId, Long userId);

    void restoreCard(Long cardId, Long userId);
}
