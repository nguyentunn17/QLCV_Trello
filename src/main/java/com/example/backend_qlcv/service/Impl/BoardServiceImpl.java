package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.entity.User;
import com.example.backend_qlcv.entity.UserBoard;
import com.example.backend_qlcv.repository.BoardRepository;
import com.example.backend_qlcv.repository.UserBoardRepository;
import com.example.backend_qlcv.repository.UserRepository;
import com.example.backend_qlcv.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserBoardRepository userBoardRepository;

    @Override
    public List<Board> getAll() {
        return boardRepository.findAll();
    }

    @Override
    public Page<Board> getPage(Pageable pageable, Long userId, String role) {
        if ("ROLE_ADMIN".equalsIgnoreCase(role)) {
            return boardRepository.getAllBoards(pageable);
        } else if ("ROLE_USER".equalsIgnoreCase(role)) {
            return boardRepository.getBoardsByUser(pageable, userId);
        }
        return Page.empty(); // Trả về trang rỗng nếu vai trò không hợp lệ
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
            if (userName.equals(user.getUsername())){
                return user.getId();
            }
        }
        return null;
    }

    @Transactional
    public void addMemberToBoard(Long boardId, Long userId, Long adderId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User adder = userRepository.findById(adderId)
                .orElseThrow(() -> new RuntimeException("Adder not found"));

        // Thêm thành viên vào board thông qua UserBoard
        UserBoard userBoard = new UserBoard();
        userBoard.setBoard(board);
        userBoard.setUser(user);
        userBoard.setRole("MEMBER");
        userBoard.setAssignedAt(new Date(System.currentTimeMillis()));

        userBoardRepository.save(userBoard);

        // Gửi email thông báo từ địa chỉ email mặc định
        String subject = "Bạn đã được thêm vào một board mới";
        String text = "Chào " + user.getFullName()+ ",\n\nBạn đã được thêm vào board \"" + board.getName() + "\" bởi " + adder.getFullName() + ".";
        emailService.sendEmail(user.getEmail(), subject, text);
    }
}
