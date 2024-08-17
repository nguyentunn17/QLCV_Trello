package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    SELECT u FROM users u WHERE (u.full_name LIKE %:kw% OR u.username LIKE %:kw% OR u.email LIKE %:kw%)
    """, nativeQuery = true)
    Page<User> searchByKeyword(Pageable pageable, @Param("kw") String keyword);

    // Không phân biệt chữ hoa chữ thường
    @Query(value = """
    SELECT * FROM users u WHERE (LOWER(u.full_name) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(u.username) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :kw, '%')))
    """, nativeQuery = true)
    List<User> searchByKW(@Param("kw") String keyword);

    @Query(value = """
        SELECT u.*
        FROM public.users u
        INNER JOIN public.user_boards ub ON u.id = ub.user_id
        WHERE ub.board_id = :boardId AND ub.status = 1
        """, nativeQuery = true)
    List<User> getUserByBoard(@Param("boardId") Long boardId);

    @Query(value = """
            SELECT u.*
        FROM public.users u
        LEFT JOIN public.user_boards ub ON u.id = ub.user_id AND ub.board_id = :boardId
        WHERE ub.user_id IS NULL OR ub.status = 0
        """, nativeQuery = true)
    List<User> getUserNotAtBoard(@Param("boardId") Long boardId);

}
