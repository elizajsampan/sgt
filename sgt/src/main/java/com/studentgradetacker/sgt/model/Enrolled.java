package com.studentgradetacker.sgt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Enrolled {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer enrolledId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "studentId", nullable = false)
    private Students students;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "courseId", nullable = false)
    private Courses courses;

    private Boolean isArchived = Boolean.FALSE;

    public Enrolled(Students students, Courses courses) {
        this.students = students;
        this.courses = courses;
        isArchived = Boolean.FALSE;
    }

}
