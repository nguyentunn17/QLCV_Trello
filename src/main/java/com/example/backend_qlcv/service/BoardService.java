package com.example.backend_qlcv.service;

import com.example.backend_qlcv.entity.Board;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoardService {
    List<Board> getAll();

    Page<Board> getPage(Integer pageNo);

    Board add(Board board);

    Board update(Board board, Long id);

    Board detail(Long id);

    void delete(Long id);

    Page<Board> search(Integer pageNo, String keyWord);
}
