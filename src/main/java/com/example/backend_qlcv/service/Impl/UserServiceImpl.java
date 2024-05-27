//package com.example.backend_qlcv.service.Impl;
//
//import com.example.backend_qlcv.entity.User;
//import com.example.backend_qlcv.repository.UserRepository;
//import com.example.backend_qlcv.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public List<User> getAll() {
//        return userRepository.findAll();
//    }
//    @Override
//    public Page<User> getPage(Integer pageNo) {
//        Pageable pageable = PageRequest.of(pageNo, 10);
//        return userRepository.getAll(pageable);
//    }
//
//    @Override
//    public User add(User user) {
//        User userSave = User.builder()
//                .email(user.getEmail())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .userName(user.getUserName())
//                .password(user.getPassword())
//                .salt(user.getSalt())
//                .status(user.getStatus())
//                .build();
//        userRepository.save(userSave);
//        return null;
//    }
//
//    @Override
//    public User update(User user, Long id) {
//        Optional<User> userOptional = userRepository.findById(id);
//        if(userOptional.isPresent()){
//            userOptional.map(userUpdate -> {
//                userUpdate.setEmail(user.getEmail());
//                userUpdate.setFirstName(user.getFirstName());
//                userUpdate.setLastName(user.getLastName());
//                userUpdate.setUserName(user.getUserName());
//                userUpdate.setPassword(user.getPassword());
//                userUpdate.setStatus(user.getStatus());
//                return userRepository.save(userUpdate);
//            }).orElse(null);
//        }
//
//        return null;
//    }
//
//    @Override
//    public User detail(Long id) {
//        return userRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    public void delete(Long id) {
//        userRepository.deleteById(id);
//    }
//}
