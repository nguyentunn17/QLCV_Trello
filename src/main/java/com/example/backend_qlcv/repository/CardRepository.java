package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query(value = """
                   SELECT * FROM public.cards WHERE name LIKE %kw%
                   ORDER BY id ASC\s
""", nativeQuery = true)
    Page<Card> searchByKeyword(Pageable pageable, @Param("kw") String keyWord);

    @Query(value = """
                   SELECT *
                                    FROM boards b
                                    JOIN lists l ON b.id = l.board_id
                                    JOIN cards c ON l.id = c.list_id
                                    WHERE c.title LIKE '%keyword%' OR c.description LIKE '%keyword%'
                                       OR l.name LIKE '%keyword%' OR b.name LIKE '%keyword%'
""", nativeQuery = true)
    List<Card> searchBoardsListsCardsByKeyword(@Param("keyword") String keyword);

    @Query("SELECT c FROM Card c where c.status = true AND c.listsId = :listId" )
    List<Card> findByListsId(Long listId);
}

