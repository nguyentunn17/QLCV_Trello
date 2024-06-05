package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.entity.ChecklistItem;
import com.example.backend_qlcv.entity.User;
import com.example.backend_qlcv.entity.UserBoard;
import com.example.backend_qlcv.repository.BoardRepository;
import com.example.backend_qlcv.repository.UserBoardRepository;
import com.example.backend_qlcv.repository.UserRepository;
import com.example.backend_qlcv.service.UserBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserBoardServiceImpl implements UserBoardService {

    @Autowired
    private UserBoardRepository userBoardRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserBoard> getAll() {
        return userBoardRepository.findAll();
    }

    @Override
    public UserBoard add(UserBoard userBoard) {
        UserBoard userBoardSave = UserBoard.builder()
                .role(userBoard.getRole())
                .assignedAt(userBoard.getAssignedAt())
                .user(userRepository.findById(getIdUser(String.valueOf(userBoard.getUser()))).get())
                .board(boardRepository.findById(getIdBoard(String.valueOf(userBoard.getBoard()))).get())
                .build();
        userBoardRepository.save(userBoardSave);
        return null;
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
