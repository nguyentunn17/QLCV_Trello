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

    @GetMapping("get-all")
    public List<Checklist> getAll() {
        return checklistService.getAll();
    }


    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") String id) {
        return new ResponseEntity(checklistService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Checklist checklist) {
        return new ResponseEntity(checklistService.add(checklist), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody Checklist checklist, @PathVariable("id") String id) {
        return new ResponseEntity(checklistService.update(checklist, Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") String id) {
        checklistService.delete(Long.valueOf(id.toString()));
    }

}
