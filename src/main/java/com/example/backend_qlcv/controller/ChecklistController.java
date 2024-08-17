package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.Checklist;
import com.example.backend_qlcv.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checklist/")
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService;


    // hiển thị checklist của card đó
    @GetMapping("card/{cardId}")
    public List<Checklist> getCheckListByCardId(@PathVariable Long cardId) {
        return checklistService.getChecklistByCardId(cardId);
    }


    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") Long id) {
        return new ResponseEntity(checklistService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    // Thêm
    @PostMapping("add")
    public ResponseEntity add(@RequestBody Checklist checklist) {
        return new ResponseEntity(checklistService.add(checklist), HttpStatus.OK);
    }

    // Cập nhật
    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@RequestBody Checklist checklist, @PathVariable Long id) {
        Checklist updateChecklist = checklistService.update(checklist, id);
        if (updateChecklist != null) {
            return ResponseEntity.ok(updateChecklist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        checklistService.delete(Long.valueOf(id.toString()));
    }

}
