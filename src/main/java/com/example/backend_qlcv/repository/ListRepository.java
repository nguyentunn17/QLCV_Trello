package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.Lists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<Lists, Long> {
}
