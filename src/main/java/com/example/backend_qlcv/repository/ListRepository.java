package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.Lists;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListRepository extends JpaRepository<Lists, Long> {
    List<Lists> findByBoardId(Long boardId);
}
