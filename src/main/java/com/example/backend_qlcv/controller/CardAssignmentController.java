package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.CardAssignment;
import com.example.backend_qlcv.service.CardAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cardassignment/")
public class CardAssignmentController {

    @Autowired
    private CardAssignmentService cardAssignmentService;

//    @GetMapping("get-all")
//    public List<CardAssignment> getAll() {
//        return cardAssignmentService.getAll();
//    }
//
//
//    @GetMapping("detail/{id}")
//    public ResponseEntity detail(@PathVariable("id") Long id) {
//        return new ResponseEntity(cardAssignmentService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
//    }
//
//    @PostMapping("add")
//    public ResponseEntity add(@RequestBody CardAssignment cardAssignment) {
//        return new ResponseEntity(cardAssignmentService.add(cardAssignment), HttpStatus.OK);
//    }
//
//    @PutMapping("update/{id}")
//    public ResponseEntity update(@RequestBody CardAssignment cardAssignment, @PathVariable("id") String id) {
//        return new ResponseEntity(cardAssignmentService.update(cardAssignment, Long.valueOf(id.toString())), HttpStatus.OK);
//    }
//
//    @DeleteMapping("delete/{id}")
//    public void delete(@PathVariable("id") Long id) {
//        cardAssignmentService.delete(Long.valueOf(id.toString()));
//    }
//

}
