package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board/")
@CrossOrigin(origins = "http://localhost:4200")
public class BoardController {

    @Autowired
    private BoardService boardServicel;

    @GetMapping("get-all")
    public List<Board> getAll() {
        return boardServicel.getAll();
    }

    @GetMapping("hien-thi")
    public Page<Board> getPage(@RequestParam(name = "pageNo",
            defaultValue = "0") Integer pageNo) {
        return boardServicel.getPage(pageNo);
    }

    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") String id) {
        return new ResponseEntity(boardServicel.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Board board) {
        return new ResponseEntity(boardServicel.add(board), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody Board board, @PathVariable("id") String id) {
        return new ResponseEntity(boardServicel.update(board, Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") String id) {
        boardServicel.delete(Long.valueOf(id.toString()));
    }

}
