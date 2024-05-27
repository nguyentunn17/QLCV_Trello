package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.constant.ERole;
import com.example.backend_qlcv.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
