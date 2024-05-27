package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.entity.ChecklistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
    @Query(value = """
            SELECT * FROM public."checklist_items"
            ORDER BY id ASC
            """, nativeQuery = true)
    Page<Board> getAll(Pageable pageable);

}
