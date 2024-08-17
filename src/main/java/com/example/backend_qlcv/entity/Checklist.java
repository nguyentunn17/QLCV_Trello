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
@Table(name = "checklists")
public class Checklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "card_id")
    private Long cardId;

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Card card;
}
