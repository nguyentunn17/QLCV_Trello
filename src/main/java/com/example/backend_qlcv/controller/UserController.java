package com.example.backend_qlcv.controller;

import com.example.backend_qlcv.entity.User;
import com.example.backend_qlcv.payload.MessageResponse;
import com.example.backend_qlcv.repository.UserBoardRepository;
import com.example.backend_qlcv.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBoardRepository userBoardRepository;

    @Autowired
    BCryptPasswordEncoder encoder;


    @GetMapping("search")
    public Page<User> search(@RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                             @RequestParam(name = "keyword") String keyword) {
        String sortDirection = "desc";
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by("id").ascending() : Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return userRepository.searchByKeyword(pageable, keyword);
    }
    @GetMapping("loc")
    public ResponseEntity search(@RequestParam(name = "keyWord") String keyWord) {
        return new ResponseEntity(userRepository.searchByKW(keyWord), HttpStatus.OK);
    }

    @GetMapping("/all3/{boardId}")
    public ResponseEntity<List<User>> getUser(@PathVariable Long boardId) {
        List<User> users = userRepository.getUserByBoard(boardId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping("/all4/{boardId}")
    public ResponseEntity<List<User>> getUserNotAtBoard(@PathVariable Long boardId) {
        List<User> users = userRepository.getUserNotAtBoard(boardId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping("all2")
    public ResponseEntity<List<User>> all() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    @GetMapping("all")
    public ResponseEntity<List<User>> all(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        List<User> users = userRepository.findAll();
        users.removeIf(user -> user.getUsername().equals(username));
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> one(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can't not found the " + id));
        return ResponseEntity.ok(user);
    }

    @GetMapping("reset/{id}")
    public ResponseEntity<?> resetPass(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can't not found the userId = " + id));

        // Mã hóa mật khẩu mới
        String hashedPassword = encoder.encode("123@123Ab");

        // Lưu mật khẩu mới vào cơ sở dữ liệu
        user.setPassword(hashedPassword);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Mật khẩu đã được đặt lại về mặc định"));
    }
    @PutMapping("update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can't not found the user with id = " + id));

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setFullName(userDetails.getFullName());
        user.setStatus(userDetails.getStatus());
        user.setRoles(userDetails.getRoles());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(encoder.encode(userDetails.getPassword()));
        }
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }

}
