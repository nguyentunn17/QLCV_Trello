package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.Attachment;
import com.example.backend_qlcv.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Query(value = """
            SELECT * FROM public."attachments"
            ORDER BY id ASC
            """, nativeQuery = true)
    Page<Board> getAll(Pageable pageable);

//    @Query(value = """
//                   SELECT * FROM public.task WHERE name LIKE %kw%
//                   ORDER BY id ASC\s
//""", nativeQuery = true)
//    Page<Board> searchByKeyword(Pageable pageable, @Param("kw") String keyWord);
}
