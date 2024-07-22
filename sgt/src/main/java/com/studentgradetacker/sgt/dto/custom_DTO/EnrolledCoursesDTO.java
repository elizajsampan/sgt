package com.studentgradetacker.sgt.dto.custom_DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnrolledCoursesDTO {

    private String courseCode;

    private String courseDescription;

    private Integer units;
}
