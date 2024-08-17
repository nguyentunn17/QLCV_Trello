package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.ChecklistItem;
import com.example.backend_qlcv.service.ChecklistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checklistItem/")
public class ChecklistItemController {
    @Autowired
    private ChecklistItemService checklistItemService;

    //Hiện thị checklistItem theo checklist
    @GetMapping("checklist/{checklistId}")
    public List<ChecklistItem> getChecklistItemByChecklistId(@PathVariable Long checklistId) {
        return checklistItemService.getChecklistItemByChecklistId(checklistId);
    }


    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") Long id) {
        return new ResponseEntity(checklistItemService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody ChecklistItem checklistItem) {
        return new ResponseEntity(checklistItemService.add(checklistItem), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@RequestBody ChecklistItem checklistItem, @PathVariable("id") Long id) {
        ChecklistItem updateChecklistItem = checklistItemService.update(checklistItem, id);
        if (updateChecklistItem != null) {
            return ResponseEntity.ok(updateChecklistItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        checklistItemService.delete(Long.valueOf(id.toString()));
    }

}
