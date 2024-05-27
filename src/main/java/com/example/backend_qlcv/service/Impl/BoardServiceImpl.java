package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.entity.User;
import com.example.backend_qlcv.repository.BoardRepository;
import com.example.backend_qlcv.repository.UserRepository;
import com.example.backend_qlcv.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Board> getAll() {
        return boardRepository.findAll();
    }

    @Override
    public Page<Board> getPage(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);
        return boardRepository.getAll(pageable);
    }

    @Override
    public Board add(Board board) {
        Board boardSave = Board.builder()
                .name(board.getName())
                .description(board.getDescription())
                .createdAt(board.getCreatedAt())
                .createdBy(userRepository.findById(getIdUser(String.valueOf(board.getCreatedBy()))).get())
                .build();
        boardRepository.save(boardSave);
        return null;
    }

    @Override
    public Board update(Board board, Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if(optionalBoard.isPresent()){
            optionalBoard.map(boardUpdate -> {
                boardUpdate.setName(board.getName());
                boardUpdate.setDescription(board.getDescription());
                boardUpdate.setCreatedAt(board.getCreatedAt());
                boardUpdate.setCreatedBy(userRepository.findById(getIdUser(String.valueOf(board.getCreatedBy()))).get());
                return boardRepository.save(boardUpdate);
            }).orElse(null);
        }
        return null;
    }


    @Override
    public Board detail(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    @Override
    public Page<Board> search(Integer pageNo, String keyWord) {
        Pageable pageable = PageRequest.of(pageNo, 10);
        return boardRepository.searchByKeyword(pageable, keyWord);
    }

    public Long getIdUser(String userName){
        for (User user  : userRepository.findAll()){
            if (userName.equals(user.getLastName())){
                return user.getId();
            }
        }
        return null;
    }
}
