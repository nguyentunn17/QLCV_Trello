package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.UserBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {
}
