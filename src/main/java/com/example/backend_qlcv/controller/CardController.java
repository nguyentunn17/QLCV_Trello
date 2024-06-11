package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.Card;
import com.example.backend_qlcv.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card/")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("get-all")
    public List<Card> getAll() {
        return cardService.getAll();
    }


    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") String id) {
        return new ResponseEntity(cardService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Card card) {
        return new ResponseEntity(cardService.add(card), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody Card card, @PathVariable("id") String id) {
        return new ResponseEntity(cardService.update(card, Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") String id) {
        cardService.delete(Long.valueOf(id.toString()));
    }

    @PostMapping("/add-member")
    public String addMemberToCard(@RequestParam Long cardId, @RequestParam Long userId) {
        cardService.addMemberToCard(cardId, userId);
        return "Member added to card and email notification sent";
    }

    @PostMapping("/{cardId}/archive")
    public ResponseEntity<Void> archiveCard(@PathVariable Long cardId, @RequestParam Long userId) {
        cardService.archiveCard(cardId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cardId}/restore")
    public ResponseEntity<Void> restoreCard(@PathVariable Long cardId, @RequestParam Long userId) {
        cardService.restoreCard(cardId, userId);
        return ResponseEntity.ok().build();
    }
}
