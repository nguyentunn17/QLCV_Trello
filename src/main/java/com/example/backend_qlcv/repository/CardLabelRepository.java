package com.example.backend_qlcv.repository;


import com.example.backend_qlcv.entity.CardLabel;
import com.example.backend_qlcv.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardLabelRepository extends JpaRepository<CardLabel, Long> {
    @Query( " SELECT l FROM Label l JOIN CardLabel cl on cl.label.id = l.id WHERE cl.card.id = :cardId")
    List<Label> findLabelsByCard(@Param("cardId") Long cardId );

    @Modifying
    @Query("DELETE FROM CardLabel cl WHERE cl.card.id = :cardId AND cl.label.id = :labelId")
    void deleteByCardIdAndLabelId(@Param("cardId") Long cardId, @Param("labelId") Long labelId);

    @Modifying
    @Query("DELETE FROM CardLabel cl WHERE cl.label.id = :labelId")
    void deleteByLabelId(@Param("labelId") Long labelId);

    @Modifying
    @Query("DELETE FROM CardLabel cl WHERE cl.card.id = :cardId")
    void deleteByCardId(@Param("cardId") Long cardId);

}
