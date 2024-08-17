package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.UserBoard;
import com.example.backend_qlcv.repository.UserBoardRepository;
import com.example.backend_qlcv.service.UserBoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userboard/")
public class UserBoardController {
    @Autowired
    private UserBoardService userBoardService;

    @Autowired
    private UserBoardRepository userBoardRepository;

    @GetMapping("get-all")
    public List<UserBoard> getAll() {
        return userBoardService.getAll();
    }


    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") Long id) {
        return new ResponseEntity(userBoardService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody UserBoard userBoard) {
        return new ResponseEntity(userBoardService.add(userBoard), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody UserBoard userBoard, @PathVariable("id") Long id) {
        return new ResponseEntity(userBoardService.update(userBoard, Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userBoardService.delete(Long.valueOf(id.toString()));
    }

    @GetMapping("role")
    public ResponseEntity<Map<String, String>> getUserRole(HttpServletRequest request, @RequestParam Long boardId) {
        Long userId = (Long) request.getAttribute("userId"); // Lấy userId từ request context
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String role = userBoardService.getUserRole(userId, boardId); // Gọi repository để lấy vai trò
        Map<String, String> response = new HashMap<>();
        response.put("role", role);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
