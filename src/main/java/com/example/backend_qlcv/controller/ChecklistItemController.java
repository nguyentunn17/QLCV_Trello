package com.example.backend_qlcv.controller;

import com.example.backend_qlcv.entity.ChecklistItem;
import com.example.backend_qlcv.repository.ChecklistItemRepository;
import com.example.backend_qlcv.service.ChecklistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checklistitem")
public class ChecklistItemController {
    @Autowired
    private ChecklistItemService checklistItemService;

    @GetMapping("get-all")
    public List<ChecklistItem> getAll() {
        return checklistItemService.getAll();
    }


    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") String id) {
        return new ResponseEntity(checklistItemService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody ChecklistItem checklistItem) {
        return new ResponseEntity(checklistItemService.add(checklistItem), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody ChecklistItem checklistItem, @PathVariable("id") String id) {
        return new ResponseEntity(checklistItemService.update(checklistItem, Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") String id) {
        checklistItemService.delete(Long.valueOf(id.toString()));
    }

}
