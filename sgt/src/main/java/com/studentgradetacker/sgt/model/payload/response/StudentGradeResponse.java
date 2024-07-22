package com.studentgradetacker.sgt.model.payload.response;

import com.studentgradetacker.sgt.dto.custom_DTO.CourseGradeDTO;
import com.studentgradetacker.sgt.dto.custom_DTO.StudentDetailsDTO;
import com.studentgradetacker.sgt.model.Students;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentGradeResponse {

    private StudentDetailsDTO student;

    private List<CourseGradeDTO> courseGrade;
}
