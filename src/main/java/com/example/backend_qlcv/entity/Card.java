package com.example.backend_qlcv.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private  Timestamp startDate;

    @Column(name = "due_date")
    private Timestamp dueDate;

    @Column(name = "position")
    private Integer position;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "list_id", referencedColumnName = "id")
    private Lists lists;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User createBy;
}
