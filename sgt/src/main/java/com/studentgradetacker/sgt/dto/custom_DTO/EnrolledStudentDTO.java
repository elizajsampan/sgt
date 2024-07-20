package com.studentgradetacker.sgt.dto.custom_DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrolledStudentDTO {

    private Integer studentId;

    private String courseDescription;

    private String courseCode;

    private Integer units;
}
