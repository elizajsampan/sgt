package com.studentgradetacker.sgt.model;

import com.studentgradetacker.sgt.enums.GradePoint;
import com.studentgradetacker.sgt.enums.GradeStatus;
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

    private String finalGradePoint;

    @Enumerated(EnumType.STRING)
    private GradeStatus gradeStatus;

    private Boolean isArchived = Boolean.FALSE;

    public Grades(Enrolled enrolled, Double prelims, Double midterms, Double finals) {
        this.enrolled = enrolled;
        this.prelims = prelims;
        this.midterms = midterms;
        this.finals = finals;
        this.finalGrade = calculateFinalGrade(prelims, midterms, finals);
        this.finalGradePoint = convertToGradePoint(this.finalGrade);
        this.gradeStatus = determineGradeStatus(prelims, midterms, finals);
    }

    private Double calculateFinalGrade(Double prelims, Double midterms, Double finals) {
        int count = 0;
        double sum = 0.0;

        if (prelims != null) {
            sum += prelims;
            count++;
        }
        if (midterms != null) {
            sum += midterms;
            count++;
        }
        if (finals != null) {
            sum += finals;
            count++;
        }

        return count > 0 ? Math.round((sum / count) * 100.0) / 100.0 : null;
    }

    private String convertToGradePoint(Double finalGrade) {
        if (finalGrade == null) {
            return "INC";
        }
        GradePoint gradePoint = GradePoint.fromFinalGrade(finalGrade);
        return String.valueOf(gradePoint.getValue());
    }

    private GradeStatus determineGradeStatus(Double prelims, Double midterms, Double finals) {
        return (prelims == null || midterms == null || finals == null) ? GradeStatus.INC : GradeStatus.COMPLETED;
    }

    public void updateGrades(Double prelims, Double midterms, Double finals) {
        if (prelims != null) this.prelims = prelims;
        if (midterms != null) this.midterms = midterms;
        if (finals != null) this.finals = finals;
        this.finalGrade = calculateFinalGrade(this.prelims, this.midterms, this.finals);
        this.gradeStatus = determineGradeStatus(this.prelims, this.midterms, this.finals);
    }
}
