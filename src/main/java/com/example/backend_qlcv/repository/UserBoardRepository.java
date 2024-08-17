package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.UserBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {

    Optional<UserBoard> findByBoardIdAndUserIdAndRole(Long boardId, Long userId, String role);

    @Query("SELECT ub.role FROM UserBoard ub WHERE ub.user.id = :userId AND ub.board.id = :boardId")
    String findRoleByUserId(@Param("userId") Long userId, @Param("boardId") Long boardId);

    @Modifying
    @Query("UPDATE UserBoard ub SET ub.status = :status WHERE ub.user.id = :userId AND ub.board.id = :boardId")
    void updateUserBoardStatus(@Param("userId") Long userId, @Param("boardId") Long boardId, @Param("status") int status);

    Optional<UserBoard> findByUserIdAndBoardId(Long userId, Long boardId);
}
