package com.example.backend_qlcv.service;

import com.example.backend_qlcv.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {
    List<Board> getAll();

    List<Board> getAllBoards();

    Board addBoardWithMembers(Board board, List<Long> userIds);

    Board update(Board board, Long id);

    Board detail(Long id);

    void delete(Long id);

    Page<Board> search(Integer pageNo, String keyWord);

    void addMembersToBoard(Long boardId, List<Long> userIds, Long adderId);

    Page<Board> loc(Integer pageNo, Integer status);

    Board updateBoardName(Long boardId, String newName);

    void closeBoard(Long boardId);

    void restoreBoard(Long boardId);

    void leaveBoard(Long boardId, Long userId);

}
