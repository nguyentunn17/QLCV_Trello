package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.Card;
import com.example.backend_qlcv.entity.CardAssignment;
import com.example.backend_qlcv.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardAssignmentRepository extends JpaRepository<CardAssignment, Long> {
    List<CardAssignment> findByCard(Card card);
    @Query(value = """
            SELECT * FROM public."card_assignments"
            ORDER BY id ASC
            """, nativeQuery = true)
    Page<CardAssignment> getAll(Pageable pageable);


    @Query(value = """
            SELECT ca.user.id FROM CardAssignment ca WHERE ca.card.id = :cardId
            """, nativeQuery = true)
    List<Long> findAssignedUserIdsByCardId(@Param("cardId") Long cardId);

    @Modifying
    @Query("DELETE FROM CardAssignment ca WHERE ca.card.id= :cardId AND ca.user.id = :userId")
    void deleteByCardIdAndUserId(@Param("cardId") Long cardId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM CardAssignment ca WHERE ca.card.id IN (" +
            "SELECT c.id FROM Card c WHERE c.lists.id IN (" +
            "SELECT l.id FROM Lists l WHERE l.board.id = :boardId)) AND ca.user.id = :userId")
    void deleteByBoardIdAndUserId(@Param("boardId") Long boardId, @Param("userId") Long userId);


    @Query("SELECT u FROM User u JOIN CardAssignment ca ON u.id = ca.user.id WHERE ca.card.id = :cardId")
    List<User> findUsersByCardId(@Param("cardId") Long cardId);

    @Modifying
    @Query("DELETE FROM CardAssignment ca WHERE ca.card.id = :cardId")
    void deleteByCardId(@Param("cardId") Long cardId);


//    @Query(value = """
//                   SELECT * FROM public.boards WHERE name LIKE %kw%
//                   ORDER BY id ASC\s
//""", nativeQuery = true)
//    Page<Board> searchByKeyword(Pageable pageable, @Param("kw") String keyWord);

}
