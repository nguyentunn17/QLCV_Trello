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
@Table(name = "checklist_items")
public class ChecklistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_comleted")
    private Integer isComleted;

    @Column(name = "position")
    private Integer position;

    @ManyToOne
    @JoinColumn(name = "checklist_id", referencedColumnName = "id")
    private Checklist checklist;
}
