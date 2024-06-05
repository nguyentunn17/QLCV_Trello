package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.Comment;
import com.example.backend_qlcv.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment/")
public class CommentController {
    @Autowired
    private CommentService commentService;

    public List<Comment> getAll() {
        return commentService.getAll();
    }


    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") String id) {
        return new ResponseEntity(commentService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Comment comment) {
        return new ResponseEntity(commentService.add(comment), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody Comment comment, @PathVariable("id") String id) {
        return new ResponseEntity(commentService.update(comment, Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") String id) {
        commentService.delete(Long.valueOf(id.toString()));
    }

}
