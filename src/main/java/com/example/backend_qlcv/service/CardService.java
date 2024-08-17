package com.example.backend_qlcv.service;


import com.example.backend_qlcv.entity.Card;
import com.example.backend_qlcv.entity.User;
import com.example.backend_qlcv.event.CommentEvent;

import java.util.List;

public interface CardService {
    List<Card> getAll();

    Card add(Card card);

    Card update(Card card, Long id);

    Card detail(Long id);

    void deleteCard(Long cardId);

    void addMemberToCard(Long cardId,List<Long> userIds, Long adderId );

    void handleCommentEvent(CommentEvent event);

    void archiveCard(Long cardId);

    void restoreCard(Long cardId);

    List<Card> getCardsByListsId(Long listId);

    void removeMemberFromCard(Long cardId, Long userId);

    List<User> getMembersInCard(Long cardId);
}
