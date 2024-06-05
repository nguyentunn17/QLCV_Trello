package com.example.backend_qlcv.repository;


import com.example.backend_qlcv.entity.CardLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardLabelRepository extends JpaRepository<CardLabel, Long> {

}
