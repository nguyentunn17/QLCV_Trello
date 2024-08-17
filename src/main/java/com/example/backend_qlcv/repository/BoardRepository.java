package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // Hiện thị các board với điều kiện còn hoạt động
    @Query(value = """
            SELECT * FROM public."boards" b WHERE b.status = 1
            ORDER BY id DESC 
            """, nativeQuery = true)
    List<Board> getAll();

    // Hiện thị các board với điều kiện là được add vào và còn hoạt động
    @Query(value = """
            SELECT b.* FROM public."boards" b 
            INNER JOIN public."user_boards" ub ON b.id = ub.board_id 
            INNER JOIN public."users" u ON ub.user_id = u.id 
            WHERE u.username = :username AND b.status = 1 AND ub.status = 1
            ORDER BY b.id DESC
            """,nativeQuery = true )
    List<Board> findBoardsByUsername(@Param("username") String username );

    //
    @Query(value = """
            SELECT * FROM public."boards" WHERE status = ?1
            ORDER BY id ASC
            """, nativeQuery = true)
    Page<Board> loc(Pageable pageable, Integer status);

    @Query(value = """ 
                   SELECT * FROM public.boards WHERE name LIKE %kw%
                   ORDER BY id ASC\s
""", nativeQuery = true)
    Page<Board> searchByKeyword(Pageable pageable, @Param("kw") String keyWord);

}
