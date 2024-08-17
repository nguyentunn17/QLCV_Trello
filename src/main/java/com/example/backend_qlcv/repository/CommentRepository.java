package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.Comment;
import com.example.backend_qlcv.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.card.id = :cardId")
    void deleteByCardId(@Param("cardId") Long cardId);
}
