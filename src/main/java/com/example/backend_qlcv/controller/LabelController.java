package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.Label;
import com.example.backend_qlcv.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/label/")
public class LabelController {
    @Autowired
    private LabelService labelService;

    @GetMapping("get-all")
    public List<Label> getAll() {
        return labelService.getAll();
    }


    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") String id) {
        return new ResponseEntity(labelService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Label label) {
        return new ResponseEntity(labelService.add(label), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody Label label, @PathVariable("id") String id) {
        return new ResponseEntity(labelService.update(label, Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") String id) {
        labelService.delete(Long.valueOf(id.toString()));
    }

}
