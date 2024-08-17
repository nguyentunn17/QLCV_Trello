package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.Card;
import com.example.backend_qlcv.entity.User;
import com.example.backend_qlcv.model.request.AddMembersRequest;
import com.example.backend_qlcv.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/card/")
public class CardController {
    private static final Logger logger = LoggerFactory.getLogger(CardController.class);
    @Autowired
    private CardService cardService;

    @GetMapping("get-all")
    public List<Card> getAll() {
        return cardService.getAll();
    }


    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") Long id) {
        return new ResponseEntity(cardService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    // Thêm card
    @PostMapping("add")
    public ResponseEntity<Card> addCard(@RequestBody Card card) {
        // Xử lý thêm thẻ
        Card newCard = cardService.add(card);
        return ResponseEntity.ok(newCard);
    }

    // Chỉnh sửa card
    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@RequestBody Card card, @PathVariable Long id) {
       Card updateCard = cardService.update(card, id);
            if (updateCard != null) {
                return ResponseEntity.ok(updateCard);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        // Xoá card
    @DeleteMapping("delete/{cardId}")
    public void delete(@PathVariable("cardId") Long cardId) {
        cardService.deleteCard(cardId);
    }

    // Thêm thành viên vào card thông qua cardAssignment
    @PostMapping("add-member")
    public ResponseEntity<Void> addMemberToCard(@RequestParam("cardId") Long cardId,
                                  @RequestBody AddMembersRequest addMembersRequest) {
        cardService.addMemberToCard(cardId, addMembersRequest.getUserIds(), addMembersRequest.getAdderId());
        return ResponseEntity.ok().build();
    }

    // Xoá thành viên ra khỏi card thông qua cardAssignment
    @DeleteMapping("{cardId}/remove-member/{userId}")
    public ResponseEntity<?> removeMemberFromCard(@PathVariable Long cardId, @PathVariable Long userId) {
        try {
            cardService.removeMemberFromCard(cardId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error removing member from card", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Hiện thị thành viên trong card thông qua cardAssignment
    @GetMapping("{cardId}/members")
    public ResponseEntity<List<User>> getMembersInCard(@PathVariable Long cardId) {
        List<User> members = cardService.getMembersInCard(cardId);
        return ResponseEntity.ok(members);
    }

    @PostMapping("{cardId}/archive")
    public ResponseEntity<Void> archiveCard(@PathVariable Long cardId) {
        cardService.archiveCard(cardId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{cardId}/restore")
    public ResponseEntity<Void> restoreCard(@PathVariable Long cardId) {
        cardService.restoreCard(cardId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("get-by-list")
    public ResponseEntity<List<Card>> getCardsByListsId(@RequestParam("listId") Long listId) {
        List<Card> cards = cardService.getCardsByListsId(listId);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }
}
