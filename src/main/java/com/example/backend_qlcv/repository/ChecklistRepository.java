package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.entity.Checklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist,Long> {
    @Query(value = """
            SELECT * FROM public."checklists"
            ORDER BY id ASC
            """, nativeQuery = true)
    Page<Board> getAll(Pageable pageable);

}
