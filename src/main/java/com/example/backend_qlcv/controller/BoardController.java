package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board/")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("get-all")
    public List<Board> getAll() {
        return boardService.getAll();
    }

    @GetMapping("/hien-thi")
    public ResponseEntity hienThi(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo) {
        return new ResponseEntity(boardService.getAllBoards(pageNo), HttpStatus.OK);
    }

    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") String id) {
        return new ResponseEntity(boardService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Board board) {
        return new ResponseEntity(boardService.add(board), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody Board board, @PathVariable("id") String id) {
        return new ResponseEntity(boardService.update(board, Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") String id) {
        boardService.delete(Long.valueOf(id.toString()));
    }

    @GetMapping("search")
    public ResponseEntity search(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo, @RequestParam(name = "keyWord") String keyWord) {
        return new ResponseEntity(boardService.search(pageNo, keyWord), HttpStatus.OK);
    }
    @GetMapping("loc")
    public ResponseEntity loc(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo, @RequestParam(name = "status")  Integer status) {
        return new ResponseEntity(boardService.loc(pageNo, status), HttpStatus.OK);
    }
    @PostMapping("{boardId}/members/{userId}/adder/{adderId}")
    public ResponseEntity<String> addMemberToBoard(
            @PathVariable Long boardId,
            @PathVariable Long userId,
            @PathVariable Long adderId) {
        boardService.addMemberToBoard(boardId, userId, adderId);
        return ResponseEntity.ok("Member added to board and email sent");
    }
}
