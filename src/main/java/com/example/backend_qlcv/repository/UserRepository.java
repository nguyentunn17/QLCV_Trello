package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    @Query(value = """
//            SELECT * FROM public."users"
//            ORDER BY id ASC
//            """, nativeQuery = true)
//    Page<User> getAll(Pageable pageable);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByPassword(String password);

    Boolean existsByEmail(String email);

    @Query(value = """
    SELECT u FROM users u WHERE (u.first_name LIKE %:kw% OR u.last_name LIKE %:kw% OR u.username LIKE %:kw% OR u.email LIKE %:kw%)
    """, nativeQuery = true)
    Page<User> searchByKeyword(Pageable pageable, @Param("kw") String keyword);
}
