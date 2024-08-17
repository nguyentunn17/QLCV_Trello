package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.entity.ChecklistItem;
import com.example.backend_qlcv.entity.User;
import com.example.backend_qlcv.entity.UserBoard;
import com.example.backend_qlcv.repository.BoardRepository;
import com.example.backend_qlcv.repository.UserBoardRepository;
import com.example.backend_qlcv.repository.UserRepository;
import com.example.backend_qlcv.service.UserBoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserBoardServiceImpl implements UserBoardService {

    @Autowired
    private UserBoardRepository userBoardRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public List<UserBoard> getAll() {
        return userBoardRepository.findAll();
    }

    @Override
    @Transactional
    public UserBoard add(UserBoard userBoard) {
        // Lấy userId từ request attribute được set bởi JwtAuthenticationFilter
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("User ID not found in request");
        }

        // Lấy đối tượng User từ ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Lấy đối tượng Board từ ID
        Board board = boardRepository.findById(userBoard.getBoard().getId())
                .orElseThrow(() -> new RuntimeException("Board not found with ID: " + userBoard.getBoard().getId()));

        UserBoard userBoardSave = UserBoard.builder()
                .role("ADMIN")
                .assignedAt(userBoard.getAssignedAt())
                .user(user)
                .board(board)
                .build();

        return userBoardRepository.save(userBoardSave);
    }





    @Override
    public UserBoard update(UserBoard userBoard, Long id) {
        Optional<UserBoard> optionalUserBoard = userBoardRepository.findById(id);
        if (optionalUserBoard.isPresent()){
            optionalUserBoard.map(userBoardUpdate -> {
                userBoardUpdate.setUser(userRepository.findById(getIdUser(String.valueOf(userBoard.getUser()))).get());
                userBoardUpdate.setBoard(boardRepository.findById(getIdBoard(String.valueOf(userBoard.getBoard()))).get());
                return userBoardRepository.save(userBoardUpdate);
            }).orElse(null);
        }

        return null;
    }

    @Override
    public UserBoard detail(Long id) {
        return userBoardRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        userBoardRepository.deleteById(id);
    }

    @Override
    public String getUserRole(Long userId, Long boardId) {
        if (userId == null) {
            throw new RuntimeException("User ID not found");
        }
        return userBoardRepository.findRoleByUserId(userId, boardId); // Gọi repository để lấy vai trò
    }

    public Long getIdUser(String userName){
        for (User user  : userRepository.findAll()){
            if (userName.equals(user.getUsername())){
                return user.getId();
            }
        }
        return null;
    }

    public Long getIdBoard(String boardName){
        for (Board board  : boardRepository.findAll()){
            if (boardName.equals(board.getName())){
                return board.getId();
            }
        }
        return null;
    }
}
