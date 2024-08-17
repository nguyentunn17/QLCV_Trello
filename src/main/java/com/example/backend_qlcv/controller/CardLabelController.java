package com.example.backend_qlcv.controller;

import com.example.backend_qlcv.entity.CardLabel;
import com.example.backend_qlcv.entity.Label;
import com.example.backend_qlcv.model.request.LabelRequest;
import com.example.backend_qlcv.service.CardLabelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cardlabel/")
public class CardLabelController {
    private static final Logger logger = LoggerFactory.getLogger(CardController.class);
    @Autowired
    private CardLabelService cardLabelService;

    // Hiện thị label đã thêm vào card
    @GetMapping("{cardId}/get-by-card")
    public ResponseEntity<List<Label>> getLabelsInCard(@PathVariable("cardId") Long cardId) {
        List<Label> labels = cardLabelService.getLabelsByCard(cardId);
        return ResponseEntity.ok(labels);
    }

    // Thêm label vào card
    @PostMapping("add-color")
    public ResponseEntity<Void> addColorsToCard(
            @RequestParam("cardId") Long cardId,
            @RequestBody LabelRequest labelRequest) {
        cardLabelService.addColorsToCard(cardId, labelRequest.getLabelIds());
        return ResponseEntity.ok().build();
    }

    //Xoá card-label ra khỏi card-label (Xoá 1)
    @DeleteMapping("{cardId}/remove-label/{labelId}")
    public ResponseEntity<?> removeLabelFromCard(@PathVariable Long cardId, @PathVariable Long labelId){
        try{
            cardLabelService.removeLabelByCard(cardId, labelId);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            logger.error("Error removing member from card", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Xoá tất cả card-label theo labelId (Xoá nhiều)
    @DeleteMapping("remove-label/{labelId}")
    public ResponseEntity<?> removeLabelFromLabelId( @PathVariable Long labelId){
        try{
            cardLabelService.removeLabelByLabelId(labelId);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            logger.error("Error removing member from card", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
