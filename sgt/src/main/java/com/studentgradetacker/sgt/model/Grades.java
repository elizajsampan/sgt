package com.studentgradetacker.sgt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Grades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gradeId;

    @ManyToOne
    @JoinColumn(name = "enrolled_id", referencedColumnName = "enrolledId", nullable = false)
    private Enrolled enrolled;

    private Double prelims;

    private Double midterms;

    private Double finals;

    private Double finalGrade;

    private Boolean isArchived;
}
