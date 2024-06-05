package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.Lists;
import com.example.backend_qlcv.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/list/")
public class ListControler {
    @Autowired
    private ListService listService;

    @GetMapping("get-all")
    public List<Lists> getAll() {
        return listService.getAll();
    }


    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") String id) {
        return new ResponseEntity(listService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Lists lists) {
        return new ResponseEntity(listService.add(lists), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody Lists lists, @PathVariable("id") String id) {
        return new ResponseEntity(listService.update(lists, Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") String id) {
        listService.delete(Long.valueOf(id.toString()));
    }

}
