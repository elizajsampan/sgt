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

    private Boolean isArchived = Boolean.FALSE;

    public Grades(Enrolled enrolled, Double prelims, Double midterms, Double finals) {
        this.enrolled = enrolled;
        this.prelims = prelims;
        this.midterms = midterms;
        this.finals = finals;
        this.finalGrade = (prelims + midterms + finals) / 3;
        this.isArchived = Boolean.FALSE;
    }
}
