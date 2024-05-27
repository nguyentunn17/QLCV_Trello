package com.example.backend_qlcv.controller;

import com.example.backend_qlcv.entity.User;
import com.example.backend_qlcv.payload.MessageResponse;
import com.example.backend_qlcv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCrypt;


import java.util.List;
import java.util.Random;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @GetMapping("/user")
    public Page<User> search(@RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                             @RequestParam(name = "keyword") String keyword){
        String sortDirection = "desc";
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by("id").ascending() : Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
        return userRepository.searchByKeyword(pageable, keyword);
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<User>> all(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> one(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can't not found the "+ id));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/reset/{id}")
    public ResponseEntity<?> resetPass(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can't not found the userId = "+ id));

        // Tạo một salt ngẫu nhiên
        String salt = BCrypt.gensalt();

        // Kết hợp mật khẩu mới với salt
        String newPasswordWithSalt = "123@123Ab" + salt;

        // Mã hóa mật khẩu kết hợp với salt mới
        String hashedPassword = BCrypt.hashpw(newPasswordWithSalt, BCrypt.gensalt());

        // Lưu mật khẩu mới vào cơ sở dữ liệu
        user.setPassword(hashedPassword);
        user.setSalt(salt);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Mật khẩu đã được đặt lại về mặc định"));
    }


}
