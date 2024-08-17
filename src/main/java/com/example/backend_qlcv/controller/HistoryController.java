package com.example.backend_qlcv.controller;



import com.example.backend_qlcv.entity.History;
import com.example.backend_qlcv.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history/")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("get-all")
    public List<History> getAll() {
        return historyService.getAll();
    }

    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") Long id) {
        return new ResponseEntity(historyService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody History history) {
        return new ResponseEntity(historyService.add(history), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody History history, @PathVariable("id") Long id) {
        return new ResponseEntity(historyService.update(history, Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        historyService.delete(Long.valueOf(id.toString()));
    }

}
