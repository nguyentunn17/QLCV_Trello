package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.Lists;
import com.example.backend_qlcv.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lists/")
public class ListControler {
    @Autowired
    private ListService listService;

    @GetMapping("get-all")
    public List<Lists> getAll() {
        return listService.getAll();
    }

    @GetMapping("detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        Lists lists = listService.detail(id);
        if (lists != null) {
            return ResponseEntity.ok(lists);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody Lists lists) {
        Lists addedList = listService.add(lists);
        return ResponseEntity.ok(addedList);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@RequestBody Lists lists, @PathVariable Long id) {
        Lists updatedList = listService.update(lists, id);
        if (updatedList != null) {
            return ResponseEntity.ok(updatedList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        listService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{listId}/archive")
    public ResponseEntity<Void> archiveList(@PathVariable Long listId, @RequestParam Long userId) {
        listService.archiveList(listId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{listId}/restore")
    public ResponseEntity<Void> restoreList(@PathVariable Long listId, @RequestParam Long userId) {
        listService.restoreList(listId, userId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/board/{boardId}")
    public List<Lists> getListsByBoardId(@PathVariable Long boardId) {
        return listService.getListsByBoardId(boardId);
    }
}
