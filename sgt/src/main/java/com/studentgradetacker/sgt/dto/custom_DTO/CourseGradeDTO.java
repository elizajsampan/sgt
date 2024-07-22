package com.studentgradetacker.sgt.dto.custom_DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseGradeDTO {

    private String courseCode;

    private String courseDescription;

    private Double prelims;

    private Double midterms;

    private Double finals;

    private Double finalGrade;

    private Double finalGradePoint;

    private String gradeStatus;
}
