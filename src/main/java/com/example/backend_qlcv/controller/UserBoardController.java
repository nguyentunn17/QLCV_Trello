package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.UserBoard;
import com.example.backend_qlcv.service.UserBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userboard/")
public class UserBoardController {
    @Autowired
    private UserBoardService userBoardService;

    @GetMapping("get-all")
    public List<UserBoard> getAll() {
        return userBoardService.getAll();
    }


    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") String id) {
        return new ResponseEntity(userBoardService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody UserBoard userBoard) {
        return new ResponseEntity(userBoardService.add(userBoard), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody UserBoard userBoard, @PathVariable("id") String id) {
        return new ResponseEntity(userBoardService.update(userBoard, Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") String id) {
        userBoardService.delete(Long.valueOf(id.toString()));
    }


}
