package com.example.backend_qlcv.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "lists")
public class Lists {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private Integer position;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "board_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Board board;

    @Column(name = "board_id")
    private Long boardId;
}
