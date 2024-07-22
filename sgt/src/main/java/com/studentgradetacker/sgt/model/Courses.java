package com.studentgradetacker.sgt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Courses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    private String courseDescription;

    private String courseCode;

    private Integer units;

    private Boolean isArchived = Boolean.FALSE;

    public Courses(String courseDescription, String courseCode, Integer units) {
        this.courseDescription = courseDescription;
        this.courseCode = courseCode;
        this.units = units;
        this.isArchived = Boolean.FALSE;
    }
}