package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.ChecklistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
    List<ChecklistItem> findByChecklistId(Long checklistId);

    @Modifying
    @Query("DELETE FROM ChecklistItem ci WHERE ci.checklistId = :checklistId ")
    void deleteByChecklistId( @Param("checklistId") Long checklistId);

    @Modifying
    @Query("DELETE FROM ChecklistItem ci WHERE ci.checklistId IN (SELECT c.id FROM Checklist c WHERE c.cardId = :cardId)")
    void deleteByCardId(@Param("cardId") Long cardId);

}
