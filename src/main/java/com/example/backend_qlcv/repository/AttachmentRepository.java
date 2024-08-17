package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Query(value = """
            SELECT * FROM public."attachments"
            ORDER BY id ASC
            """, nativeQuery = true)
    Page<Attachment> getAll(Pageable pageable);

    @Modifying
    @Query("DELETE FROM Attachment a WHERE a.card.id = :cardId")
    void deleteByCardId(@Param("cardId") Long cardId);

//    @Query(value = """
//                   SELECT * FROM public.task WHERE name LIKE %kw%
//                   ORDER BY id ASC\s
//""", nativeQuery = true)
//    Page<Board> searchByKeyword(Pageable pageable, @Param("kw") String keyWord);
}
