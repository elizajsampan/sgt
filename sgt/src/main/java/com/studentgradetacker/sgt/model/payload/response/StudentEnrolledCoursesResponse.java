package com.studentgradetacker.sgt.model.payload.response;

import com.studentgradetacker.sgt.dto.custom_DTO.EnrolledCoursesDTO;
import com.studentgradetacker.sgt.dto.custom_DTO.StudentDetailsDTO;
import com.studentgradetacker.sgt.model.Students;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentEnrolledCoursesResponse {

    private StudentDetailsDTO student;

    private List<EnrolledCoursesDTO> enrolledCourses;
}
