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

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "position")
    private Integer position;

    @Column(name = "checklist_id")
    private Long checklistId;

    @ManyToOne
    @JoinColumn(name = "checklist_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Checklist checklist;
}
