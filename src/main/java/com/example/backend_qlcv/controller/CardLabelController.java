package com.example.backend_qlcv.controller;

import com.example.backend_qlcv.entity.CardLabel;
import com.example.backend_qlcv.service.CardLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cardlabel/")
public class CardLabelController {

    @Autowired
    private CardLabelService cardLabelService;

    @GetMapping("get-all")
    public List<CardLabel> getAll() {
        return cardLabelService.getAll();
    }


    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") String id) {
        return new ResponseEntity(cardLabelService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody CardLabel cardLabel) {
        return new ResponseEntity(cardLabelService.add(cardLabel), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody CardLabel cardLabel, @PathVariable("id") String id) {
        return new ResponseEntity(cardLabelService.update(cardLabel, Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") String id) {
        cardLabelService.delete(Long.valueOf(id.toString()));
    }


}
