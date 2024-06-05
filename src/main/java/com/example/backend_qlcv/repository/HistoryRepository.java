package com.example.backend_qlcv.repository;


import com.example.backend_qlcv.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query(value = """
            SELECT * FROM public."boards"
            ORDER BY id ASC
            """, nativeQuery = true)
    Page<History> getAll(Pageable pageable);

    @Query(value = """ 
                   SELECT * FROM public."history" WHERE name LIKE %kw%
                   ORDER BY id ASC\s
""", nativeQuery = true)
    Page<History> searchByKeyword(Pageable pageable, @Param("kw") String keyWord);
}
