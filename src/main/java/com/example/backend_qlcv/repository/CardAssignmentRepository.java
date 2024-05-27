package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.entity.CardAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardAssignmentRepository extends JpaRepository<CardAssignment, Long> {
    @Query(value = """
            SELECT * FROM public."card_assignments"
            ORDER BY id ASC
            """, nativeQuery = true)
    Page<Board> getAll(Pageable pageable);

//    @Query(value = """
//                   SELECT * FROM public.boards WHERE name LIKE %kw%
//                   ORDER BY id ASC\s
//""", nativeQuery = true)
//    Page<Board> searchByKeyword(Pageable pageable, @Param("kw") String keyWord);
}
