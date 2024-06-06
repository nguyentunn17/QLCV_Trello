package com.example.backend_qlcv.repository;

import com.example.backend_qlcv.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = """
            SELECT * FROM public."boards"
            ORDER BY id ASC
            """, nativeQuery = true)
    Page<Board> getAll(Pageable pageable);
    @Query(value = """ 
                   SELECT b.* FROM public.boards b
                   JOIN public.user_boards ub ON b.id = ub.board_id
                   JOIN public.users u ON ub.user_id = u.id
                   WHERE u.username = :username
                   ORDER BY b.id ASC
                   """, nativeQuery = true)
    Page<Board> findBoardsByUsername(@Param("username") String username, Pageable pageable);

    @Query(value = """ 
                   SELECT * FROM public.boards WHERE name LIKE %kw%
                   ORDER BY id ASC\s
""", nativeQuery = true)
    Page<Board> searchByKeyword(Pageable pageable, @Param("kw") String keyWord);

}
