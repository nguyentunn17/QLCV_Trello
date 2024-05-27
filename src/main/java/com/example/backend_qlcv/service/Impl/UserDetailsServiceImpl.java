package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.User;
import com.example.backend_qlcv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    public void createUser(User user) {
        String salt = BCrypt.gensalt(); // Tạo salt
        String saltedPassword = user.getPassword() + salt; // Kết hợp mật khẩu với salt
        String hashedPassword = BCrypt.hashpw(saltedPassword, BCrypt.gensalt()); // Mã hóa mật khẩu với salt

        user.setPassword(hashedPassword);
        user.setSalt(salt);

        userRepository.save(user);
    }
}
